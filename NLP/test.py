import save_train as s

example="This is a very nice flat with so much space and luxury"
print(s.sentiment(example))				# Call sentiment function of save_train and print type(pos/neg) and confidence percent

example="worst. very bad. pathetic. stinking"
print(s.sentiment(example))

example="This is not at all good"
print(s.sentiment(example))

example="I love it. highly recommended"
print(s.sentiment(example))
