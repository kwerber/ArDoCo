package edu.kit.ipd.consistency_analyzer.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.kit.ipd.consistency_analyzer.datastructures.DependencyTag;
import edu.kit.ipd.consistency_analyzer.datastructures.IWord;
import edu.kit.ipd.consistency_analyzer.datastructures.PosTag;

public final class WordHelper {

	private WordHelper() {
		throw new IllegalAccessError();
	}

	public static boolean hasDeterminerAsPreWord(IWord word) {

		IWord preWord = word.getPreWord();
		if (preWord == null) {
			return false;
		}

		PosTag prePosTag = preWord.getPosTag();
		if (PosTag.DT.equals(prePosTag)) {
			return true;
		}
		return false;
	}

	public static boolean hasIndirectDeterminerAsPreWord(IWord word) {
		if (hasDeterminerAsPreWord(word) && (word.getText().equalsIgnoreCase("a") || word.getText().equalsIgnoreCase("an"))) {
			return true;
		}
		return false;
	}

	public static List<DependencyTag> getIncomingDependencyTags(IWord word) {
		return Arrays.stream(DependencyTag.values()).filter(d -> word.getWordsThatAreDependentOnThis(d).size() > 0).collect(Collectors.toList());
	}

	public static List<DependencyTag> getOutgoingDependencyTags(IWord word) {
		return Arrays.stream(DependencyTag.values()).filter(d -> word.getWordsThatAreDependencyOfThis(d).size() > 0).collect(Collectors.toList());
	}
}
