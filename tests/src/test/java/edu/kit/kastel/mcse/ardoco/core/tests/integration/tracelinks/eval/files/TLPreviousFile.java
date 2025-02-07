/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.core.tests.integration.tracelinks.eval.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import edu.kit.kastel.mcse.ardoco.core.tests.Project;
import edu.kit.kastel.mcse.ardoco.core.tests.integration.tracelinks.eval.TLProjectEvalResult;
import edu.kit.kastel.mcse.ardoco.core.tests.integration.tracelinks.eval.TestLink;

public class TLPreviousFile {

    public static Collection<TLProjectEvalResult> load(Path sourceFile) throws IOException {
        List<String> lines = Files.readAllLines(sourceFile);
        Map<Project, List<TestLink>> foundLinkMap = new HashMap<>();
        List<TLProjectEvalResult> results = new ArrayList<>();

        for (String line : lines) {
            var parts = line.split(",");
            Project project = Project.valueOf(parts[0]);
            String modelId = parts[1];
            int sentenceNr = Integer.parseInt(parts[2]);

            var testLink = new TestLink(modelId, sentenceNr);

            if (!foundLinkMap.containsKey(project)) {
                foundLinkMap.put(project, new ArrayList<>());
            }

            foundLinkMap.get(project).add(testLink);
        }

        for (Project project : foundLinkMap.keySet()) {
            var correctLinks = TLGoldStandardFile.loadLinks(project);
            var foundLinks = foundLinkMap.get(project);

            results.add(new TLProjectEvalResult(project, foundLinks, correctLinks));
        }

        return results;
    }

    public static void save(Path targetFile, Collection<TLProjectEvalResult> results) throws IOException {
        if (Files.exists(targetFile)) {
            return; // do not overwrite
        }

        var sortedResults = results.stream().sorted().toList();

        var builder = new StringBuilder();

        for (TLProjectEvalResult result : sortedResults) {
            for (TestLink foundLink : result.getFoundLinks()) {
                builder.append(result.getProject().name());
                builder.append(',');
                builder.append(foundLink.modelId());
                builder.append(',');
                builder.append(foundLink.sentenceNr());
                builder.append('\n');
            }
        }

        Files.writeString(targetFile, builder.toString(), StandardOpenOption.CREATE);
    }

}
