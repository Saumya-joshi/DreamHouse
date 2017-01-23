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

pos_reviews=open("pos.txt","r").read()
neg_reviews=open("neg.txt","r").read()

all_words = []
all_pos_words = []					# All positive words
all_neg_words = []					# All negative words
document_with_review_type = []
allowed_word_types = ["J","R"]  			# J->Adjective #R->Adverb #V->Verb

for s in pos_reviews.split('\n'):			# Split the reviews bye Line
        document_with_review_type.append((s,"pos"))	# Store the pair:(review,type) in documents with type as positive
        words=word_tokenize(s)				# Split the review by words
        tagged=nltk.pos_tag(words)			# Tag the words with its part of speech and store the pair:(word,pos) in tagged
        for w in tagged:
                if w[1][0] in allowed_word_types:	# If the word is any of J, R or V then add it in the list of all_pos_words
                        all_words.append(w[0].lower())
			#all_pos_words.append(w[0].lower())

for s in neg_reviews.split('\n'):
	document_with_review_type.append((s,"neg"))	# Store the pair:(review,type) in documents with type as negative
	words=word_tokenize(s)
	tagged=nltk.pos_tag(words)
	for w in tagged:
		if w[1][0] in allowed_word_types:	 # If the word is any of J, R or V then add it in the list of all_neg_words
			all_words.append(w[0].lower())
			#all_neg_words.append(w[0].lower())

all_words=nltk.FreqDist(all_words)

word_features=list(all_words.keys())
#print(word_features)
#print(".........................................")
#print(all_words.most_common(1000))

#print("All Positive words are........................")
#print(all_pos_words)	
#all_pos_words=nltk.FreqDist(all_pos_words)
#all_distinct_pos_words=list(all_pos_words.keys())
#print("All Positive words with frequency are........................")	
#print(all_distinct_pos_words)
#print(all_pos_words.most_common(1000))
#print(all_pos_words["good"])

#print("All Negative words are........................")
#print(all_neg_words)
#all_neg_words=nltk.FreqDist(all_neg_words)
#all_distinct_neg_words=list(all_neg_words.keys())
#print("All Negative words with frequency are.........................")
#print(all_neg_words.most_common(1000))

save_documents=open("documents.pickle","wb")
pickle.dump(document_with_review_type,save_documents)
save_documents.close()

save_word_features=open("word_features.pickle","wb")
pickle.dump(word_features,save_word_features)
save_word_features.close()

def find_features(document):
	words=word_tokenize(document)
	features={}
	for w in word_features:
		features[w]=(w in words)

	return features

featuresets= [(find_features(rev),category) for (rev,category) in document_with_review_type]

save_featuresets=open("featuresets.pickle","wb")
pickle.dump(featuresets,save_featuresets)
save_featuresets.close()

#print(featuresets)

random.shuffle(featuresets)

print(len(featuresets))
#print("featuresets[good]=",featuresets["good"])

print("Training set...............")
training_set=featuresets[:100]
#print(training_set)
print("Testing set................")
testing_set=featuresets[100:]
#print(testing_set)

classifier=nltk.NaiveBayesClassifier.train(training_set)
print("Original Naive Bayes Algo accuracy percentage: ",(nltk.classify.accuracy(classifier,testing_set))*100)
#classifier.show_most_informative_features(15)
#print(classifier)
save_classifier=open("originalNaiveBayes.pickle","wb")
pickle.dump(classifier,save_classifier)
save_classifier.close()

MNB_classifier=SklearnClassifier(MultinomialNB())
MNB_classifier.train(training_set)
print("MNB_classifier accuracy percentage: ",(nltk.classify.accuracy(MNB_classifier,testing_set))*100)
#print(MNB_classifier)
save_classifier=open("MNB_classifier.pickle","wb")
pickle.dump(MNB_classifier,save_classifier)
save_classifier.close()

BernoulliNB_classifier=SklearnClassifier(BernoulliNB())
BernoulliNB_classifier.train(training_set)
print("BernoulliNB_classifier accuracy percentage: ",(nltk.classify.accuracy(BernoulliNB_classifier,testing_set))*100)
#print(BernoulliNB_classifier)
save_classifier=open("BernoulliNB_classifier.pickle","wb")
pickle.dump(BernoulliNB_classifier,save_classifier)
save_classifier.close()

LogisticRegression_classifier=SklearnClassifier(LogisticRegression())
LogisticRegression_classifier.train(training_set)
print("LogisticRegression_classifier accuracy percentage: ",(nltk.classify.accuracy(LogisticRegression_classifier,testing_set))*100)
#print(LogisticRegression_classifier)
save_classifier=open("LogisticRegression_classifier.pickle","wb")
pickle.dump(LogisticRegression_classifier,save_classifier)
save_classifier.close()

LinearSVC_classifier=SklearnClassifier(LinearSVC())
LinearSVC_classifier.train(training_set)
print("LinearSVC_classifier accuracy percentage: ",(nltk.classify.accuracy(LinearSVC_classifier,testing_set))*100)
#print(LinearSVC_classifier)
save_classifier=open("LinearSVC_classifier.pickle","wb")
pickle.dump(LinearSVC_classifier,save_classifier)
save_classifier.close()

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
