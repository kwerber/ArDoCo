/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.api.data.text;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * The Interface IWord defines a word in a text.
 */
public interface IWord {

    /**
     * Gets the sentence number.
     *
     * @return the sentence number
     */
    int getSentenceNo();

    /**
     * Return the sentence the word is contained in
     *
     * @return the sentence the word is contained in
     */
    ISentence getSentence();

    /**
     * Gets the text representation of the word.
     *
     * @return the text
     */
    String getText();

    /**
     * Gets the pos tag.
     *
     * @return the pos tag
     */
    POSTag getPosTag();

    /**
     * Gets the previous word.
     *
     * @return the previous word
     */
    IWord getPreWord();

    /**
     * Gets the next word.
     *
     * @return the next word
     */
    IWord getNextWord();

    /**
     * Gets the position in the sentence / text.
     *
     * @return the position
     */
    int getPosition();

    /**
     * Gets the lemmatized version of the word.
     *
     * @return the lemma
     */
    String getLemma();

    /**
     * Gets the words that are dependency of this.
     *
     * @param dependencyTag the dependency tag
     * @return the words that are dependency of this
     */
    ImmutableList<IWord> getOutgoingDependencyWordsWithType(DependencyTag dependencyTag);

    /**
     * Gets the words that are dependent on this.
     *
     * @param dependencyTag the dependency tag
     * @return the words that are dependent on this
     */
    ImmutableList<IWord> getIncomingDependencyWordsWithType(DependencyTag dependencyTag);
}
