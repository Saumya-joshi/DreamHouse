package com.example.pri2si17.dreamhouse; /**
 * <p>See
 * <a href="http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates#Java">
 * http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates#Java</a>
 * for the GeoLocation class referenced from this code.</p>
 *
 * @author Jan Philip Matuschek
 * @version 26 May 2010
 */
import java.util.*;
import java.io.*;
public class GeoLocationDemo {

	/**
	 * @param radius radius of the sphere.
	 * @param location center of the query circle.
	 * @param distance radius of the query circle.
	 * @param connection an SQL connection.
	 * @return places within the specified distance from location.
	 */
	public static java.sql.ResultSet findPlacesWithinDistance1(
			double radius, GeoLocation location, double distance,
			java.sql.Connection connection) throws java.sql.SQLException {

		GeoLocation[] boundingCoordinates =
			location.boundingCoordinates(distance, radius);
		boolean meridian180WithinDistance =
			boundingCoordinates[0].getLongitudeInRadians() >
			boundingCoordinates[1].getLongitudeInRadians();

		java.sql.PreparedStatement statement = connection.prepareStatement(
			"SELECT * FROM Places WHERE (Lat >= ? AND Lat <= ?) AND (Lon >= ? " +
			(meridian180WithinDistance ? "OR" : "AND") + " Lon <= ?) AND " +
			"acos(sin(?) * sin(Lat) + cos(?) * cos(Lat) * cos(Lon - ?)) <= ?");
		statement.setDouble(1, boundingCoordinates[0].getLatitudeInRadians());
		statement.setDouble(2, boundingCoordinates[1].getLatitudeInRadians());
		statement.setDouble(3, boundingCoordinates[0].getLongitudeInRadians());
		statement.setDouble(4, boundingCoordinates[1].getLongitudeInRadians());
		statement.setDouble(5, location.getLatitudeInRadians());
		statement.setDouble(6, location.getLatitudeInRadians());
		statement.setDouble(7, location.getLongitudeInRadians());
		statement.setDouble(8, distance / radius);
		return statement.executeQuery();
	}

	public static boolean findPlacesWithinDistance(double radius, GeoLocation location, GeoLocation loc1, double distance)
	{
		GeoLocation[] boundingCoordinates =
			location.boundingCoordinates(distance, radius);
		boolean meridian180WithinDistance =
			boundingCoordinates[0].getLongitudeInRadians() >
			boundingCoordinates[1].getLongitudeInRadians();
		
		double Lat,Lon;
		Lat=loc1.getLatitudeInRadians();
		Lon=loc1.getLongitudeInRadians();

		if(meridian180WithinDistance){
			if((Lat>=boundingCoordinates[0].getLatitudeInRadians() && Lat<=boundingCoordinates[1].getLatitudeInRadians()) &&
			(Lon>=boundingCoordinates[0].getLongitudeInRadians() || Lon<=boundingCoordinates[1].getLongitudeInRadians()) &&
			(Math.acos(Math.sin(location.getLatitudeInRadians())*Math.sin(Lat) + Math.cos(location.getLatitudeInRadians()) *Math.cos(Lat)*Math.cos(Lon-location.getLongitudeInRadians()))<=(distance / radius)))
			{
				return true;
			}		
			else
			{
				return false;
			}
		}
		else{
			if((Lat>=boundingCoordinates[0].getLatitudeInRadians() && Lat<=boundingCoordinates[1].getLatitudeInRadians()) &&
			(Lon>=boundingCoordinates[0].getLongitudeInRadians() && Lon<=boundingCoordinates[1].getLongitudeInRadians()) &&
			(Math.acos(Math.sin(location.getLatitudeInRadians())*Math.sin(Lat) + Math.cos(location.getLatitudeInRadians()) *Math.cos(Lat)*Math.cos(Lon-location.getLongitudeInRadians()))<=(distance / radius)))
			{
				return true;
			}		
			else
			{
				return false;
			}
		}

		/*java.sql.PreparedStatement statement = connection.prepareStatement(
			"SELECT * FROM Places WHERE (Lat >= ? AND Lat <= ?) AND (Lon >= ? " +
			(meridian180WithinDistance ? "OR" : "AND") + " Lon <= ?) AND " +
			"acos(sin(?) * sin(Lat) + cos(?) * cos(Lat) * cos(Lon - ?)) <= ?");
		statement.setDouble(1, boundingCoordinates[0].getLatitudeInRadians());
		statement.setDouble(2, boundingCoordinates[1].getLatitudeInRadians());
		statement.setDouble(3, boundingCoordinates[0].getLongitudeInRadians());
		statement.setDouble(4, boundingCoordinates[1].getLongitudeInRadians());
		statement.setDouble(5, location.getLatitudeInRadians());
		statement.setDouble(6, location.getLatitudeInRadians());
		statement.setDouble(7, location.getLongitudeInRadians());
		statement.setDouble(8, distance / radius);
		return statement.executeQuery();*/
	}

	/*public static void main(String[] args) {

		double earthRadius = 6371.01;
		GeoLocation myLocation = GeoLocation.fromDegrees(34.0, 77.0);
		double distance = 5600;		// in kms
		//java.sql.Connection connection = ...;

		findPlacesWithinDistance(earthRadius, myLocation, distance);

	}*/

}
