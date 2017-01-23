import nltk
import random
import pickle
from nltk.classify.scikitlearn import SklearnClassifier
from sklearn.naive_bayes import MultinomialNB,BernoulliNB
from sklearn.linear_model import LogisticRegression
from sklearn.svm import LinearSVC
from nltk.classify import ClassifierI
from statistics import mode
from nltk import word_tokenize

class ConfidenceCalculator(ClassifierI):
	def __init__(self,*classifiers):
		self.all_classifiers=classifiers

	def classify(self,features):
		votes=[]
		for c in self.all_classifiers:
			v=c.classify(features)
			votes.append(v)
			print(votes)
		print(mode(votes))
		return mode(votes)

	def confidence(self,features):
		votes=[]
		for c in self.all_classifiers:
			v=c.classify(features)
			votes.append(v)

		best_choice=votes.count(mode(votes))
		confidence_level=best_choice/len(votes)
		return confidence_level


documents_file=open("documents.pickle","rb")
documents_with_review_type=pickle.load(documents_file)
documents_file.close()


word_features_file=open("word_features.pickle","rb")
word_features=pickle.load(word_features_file)
word_features_file.close()


def find_features(document):
	words=word_tokenize(document)
	features={}
	for w in word_features:
		features[w]=(w in words)

	return features


featuresets_file=open("featuresets.pickle","rb")
featuresets=pickle.load(featuresets_file)
featuresets_file.close()

#print(featuresets)

random.shuffle(featuresets)

print(len(featuresets))
#print("featuresets[good]=",featuresets["good"])

training_set=featuresets[:100]
testing_set=featuresets[100:]


file_open=open("originalNaiveBayes.pickle","rb")
classifier=pickle.load(file_open)
file_open.close()


file_open=open("MNB_classifier.pickle","rb")
MNB_classifier=pickle.load(file_open)
file_open.close()


file_open=open("BernoulliNB_classifier.pickle","rb")
BernoulliNB_classifier=pickle.load(file_open)
file_open.close()


file_open=open("LogisticRegression_classifier.pickle","rb")
LogisticRegression_classifier=pickle.load(file_open)
file_open.close()


file_open=open("LinearSVC_classifier.pickle","rb")
LinearSVC_classifier=pickle.load(file_open)
file_open.close()


voted_classifier=ConfidenceCalculator(  classifier,
					LinearSVC_classifier,
					MNB_classifier,
					BernoulliNB_classifier,
					LogisticRegression_classifier)

def sentiment(text):
	feats=find_features(text)
	return voted_classifier.classify(feats),voted_classifier.confidence(feats)

#example="This is a very nice flat with so much space and luxury"
#print(sentiment(example))
#
#example="worst. very bad. pathetic. stinking"
#print(sentiment(example))
#
#example="This is not at all good"
#print(sentiment(example))
#
#example="I love it. highly recommended"
#print(sentiment(example))
