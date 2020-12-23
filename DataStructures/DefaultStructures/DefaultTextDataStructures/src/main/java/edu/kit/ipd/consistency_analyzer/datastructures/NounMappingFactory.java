package edu.kit.ipd.consistency_analyzer.datastructures;

import java.util.List;

public class NounMappingFactory {

	/**
	 * Creates a mapping dependent on the kind and a single node
	 *
	 * @param n           node for the mapping
	 * @param probability probability of being a mapping of the kind
	 * @param kind        the kind
	 * @param reference   the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created mapping
	 */
	public static INounMapping createMappingTypeNode(IWord n, String reference, MappingKind kind, double probability, List<String> occurrences) {
		INounMapping nnm;
		if (kind.equals(MappingKind.NAME)) {
			nnm = createNameNode(List.of(n), probability, reference, occurrences);
		} else if (kind.equals(MappingKind.TYPE)) {
			nnm = createTypeNode(List.of(n), probability, reference, occurrences);
		} else {
			nnm = createNortNode(List.of(n), probability, reference, occurrences);
		}
		return nnm;
	}

	/**
	 * Creates a name mapping
	 *
	 * @param nodes       nodes for the mapping
	 * @param probability probability of being a name mapping
	 * @param name        the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created name mapping
	 */
	public static INounMapping createNameNode(List<IWord> nodes, double probability, String name, List<String> occurrences) {
		return new NounMapping(nodes, probability, MappingKind.NAME, name, occurrences);
	}

	/**
	 * Creates a name mapping, based on a single node
	 *
	 * @param node        node for the mapping
	 * @param probability probability of being a name mapping
	 * @param type        the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created name mapping
	 */
	public static INounMapping createNameMapping(IWord node, double probability, String type, List<String> occurrences) {
		return new NounMapping(List.of(node), probability, MappingKind.NAME, type, occurrences);
	}

	/**
	 * Creates a type mapping
	 *
	 * @param node        node for the mapping
	 * @param probability probability of being a type mapping
	 * @param type        the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created type mapping
	 */
	public static INounMapping createTypeMapping(IWord node, double probability, String type, List<String> occurrences) {
		return new NounMapping(List.of(node), probability, MappingKind.TYPE, type, occurrences);
	}

	/**
	 * Creates a type mapping, based on a single node
	 *
	 * @param nodes       nodes for the mapping
	 * @param probability probability of being a type mapping
	 * @param type        the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created type mapping
	 */
	public static INounMapping createTypeNode(List<IWord> nodes, double probability, String type, List<String> occurrences) {
		return new NounMapping(nodes, probability, MappingKind.TYPE, type, occurrences);
	}

	/**
	 * Creates a name or type mapping, based on a single node
	 *
	 * @param node        node for the mapping
	 * @param probability probability of being a name or type mapping
	 * @param type        the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created name or type mapping
	 */
	public static INounMapping createNortMapping(IWord node, double probability, String type, List<String> occurrences) {
		return new NounMapping(List.of(node), probability, MappingKind.NAME_OR_TYPE, type, occurrences);
	}

	/**
	 * Creates a name or type mapping
	 *
	 * @param nodes       nodes for the mapping
	 * @param probability probability of being a name or type mapping
	 * @param ref         the reference for this mapping
	 * @param occurrences the appearances of the mapping
	 * @return the created name or type mapping
	 */
	public static INounMapping createNortNode(List<IWord> nodes, double probability, String ref, List<String> occurrences) {
		return new NounMapping(nodes, probability, MappingKind.NAME_OR_TYPE, ref, occurrences);
	}

}
