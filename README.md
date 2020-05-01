# word-lists

An application to aid in vocabulary and language learning.

The user can input and store words that they encounter and aren't familiar with. Once stored, accessing a word will provide a definition, synonyms, and an example of that word in a sentence.

Our app includes a basic UI to add and remove words as the home page. When the user is done adding words or wants to review their list so far, they can switch to a  "Review" screen that contains each of the words they've added. Clicking on the accompanying "Display" button next to a word will bring up that word, its definitions and synonyms at the top of the screen.

The user can toggle this information display on and off, as well as remove words from their list. If they need to add more words to their list, they can switch back to the home screen and their list will be saved.


Our app restricts input words to those defined in the [Merriam Webster Collegiate Dictionary API](https://dictionaryapi.com/products/api-collegiate-dictionary). Some of the definitions and synonyms used in our project aren't very apt, but improving the accuracy of these terms was outside of the scope and timeframe of this project.

It was slightly difficult to use these dictionary and thesaurus APIs due to the obtuse documentation and unformatted test JSON. This effect was amplified by difficulties we had when trying to parse JSON in our app. Coming from a Java background, we initially attempted to use Gson to parse the JSON response from the Merriam Webster APIs. This ended up being more complex that anticipated and required the use of carefully structured data classes and several deprecated libraries.

We resorted to using Jackson for parsing instead. Although the initial switch was confusing, it was simple to start parsing once we had configured the appropriate `Serializable` data classes. We ended up having a hierarchy of data classes to represent each vocabulary word. 

The term itself is represented by the **Vocab** class which contains two members: an instance of **Entry** and **ThesaurusEntry**. Each of these members is a separate data class to store the result of different API requests.

Instances of **Entry** contain three member variables: an instance of **HeadWordInformation**, **Pronunciations**, and a `List<String>` to represent the possible definitions for the original term. **HeadWordInformation** contains the term itself as a `String` with asterisks separating the syllables in the most common English pronunciation. 

**Pronunciation** is just a container class for **Sound** so that the JSON could be deserialized correctly by Jackson. **Sound** contains several `String`s that encode the URL for the English pronunciation sound file of the original term.

**ThesaurusEntry** is similarly a container class for **Synonyms** to facilitate parsing. **Synonyms** contains a `List<List<String>>` which represents a list of synonyms for each common meaning of the original word.

Each one of these data classes also contains a `List<String>` variable named `properties` as a fallback for `JSONAnySetter` from Jackson and the classes themselves are all `Serializable`.

We managed to accomplish most of the goals we set out for this project, but there were a few things we did not manage to achieve. 

1) We wanted to store the user's list using SQL so that it could persist between apps, but this was too challenging.
2) Some of the UI properties were difficult to wrangle. Specifically, the gravity property for several text boxes and the layout weights of buttons.
3) Although we did correctly parse the URL for each word's sound file we did not actually implement sound in the app. This would probably be the highest-priority goal if we continue to improve this app.
