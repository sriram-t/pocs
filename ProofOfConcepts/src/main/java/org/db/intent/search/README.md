https://www.youtube.com/watch?v=JL9x9N6YSUc&list=PLsdq-3Z1EPT0RrDebPvBNlmuXDfT6Qs2T&index=37

....Need to do POC for this.

- Give corpus to Byte-Pair encoding to get tokens.
- Convert tokens to vec using Word2Vec.
- Train neural network on above dataset using Bidirectional LSTM (with crf).
- Use "sequence tagging" for named entity recognition.