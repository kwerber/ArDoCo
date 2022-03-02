/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.core.tests.integration.tracelinks.eval.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.common.AgentDatastructure;
import edu.kit.kastel.mcse.ardoco.core.model.IModelInstance;
import edu.kit.kastel.mcse.ardoco.core.tests.Project;

public class TLModelFile {

    public static void save(Path targetFile, Map<Project, AgentDatastructure> dataMap) throws IOException {
        var projects = dataMap.keySet().stream().sorted().toList();
        var builder = new StringBuilder();

        for (Project project : projects) {
            var projectData = dataMap.get(project);
            String modelId = project.getModel().getModelId();
            ImmutableList<IModelInstance> models = projectData.getModelState(modelId).getInstances();

            builder.append("# ").append(project.name()).append("\n\n");

            for (IModelInstance model : models) {
                builder.append("- [")
                        .append(model.getUid())
                        .append("]: \"")
                        .append(model.getLongestName())
                        .append("\" (")
                        .append(model.getLongestType())
                        .append(") (")
                        .append(String.join(", ", model.getNames()))
                        .append(") (")
                        .append(String.join(", ", model.getTypes()))
                        .append(")\n");
            }

            builder.append("\n\n");
        }

        Files.writeString(targetFile, builder.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
