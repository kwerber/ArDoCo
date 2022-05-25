/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.core.tests.integration.tracelinks.eval;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.common.util.CommonTextToolsConfig;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.WordSimLoader;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.fastText.DL4JFastTextDataSource;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.fastText.FastTextMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.glove.GloveMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.jarowinkler.JaroWinklerMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.levenshtein.LevenshteinMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.nasari.NasariMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.nasari.babelnet.BabelNetDataSource;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.ngram.NgramMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.sewordsim.SEWordSimDataSource;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.sewordsim.SEWordSimMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.wordnet.Ezzikouri;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.measures.wordnet.WordNetMeasure;
import edu.kit.kastel.mcse.ardoco.core.common.util.wordsim.vector.VectorSqliteDatabase;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.uniba.di.lacam.kdde.lexical_db.ILexicalDatabase;
import edu.uniba.di.lacam.kdde.lexical_db.MITWordNet;
import edu.uniba.di.lacam.kdde.ws4j.similarity.JiangConrath;
import edu.uniba.di.lacam.kdde.ws4j.similarity.LeacockChodorow;
import edu.uniba.di.lacam.kdde.ws4j.similarity.Lesk;
import edu.uniba.di.lacam.kdde.ws4j.util.WS4JConfiguration;

public class ExhaustiveEvaluation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExhaustiveEvaluation.class);

    @Test
    public void run() throws IOException, SQLException {
        var overwriteExistingResults = false;
        var plans = getPlans();
        var resultDir = Path.of("results/json/");
        var csvDir = Path.of("results/csv/");

        Files.createDirectories(resultDir);
        Files.createDirectories(csvDir);

        WordSimLoader.PREVENT_LOAD = true;

        new Evaluator(plans, resultDir, overwriteExistingResults).execute();

        new EvalCSVGenerator(resultDir, csvDir, overwriteExistingResults).run();
    }

    public static List<EvalPlan> getPlans() throws IOException, SQLException {
        var plans = new ArrayList<EvalPlan>();

        if (CommonTextToolsConfig.JAROWINKLER_ENABLED) {
            plans.addAll(getJaroWinklerPlans());
        }

        if (CommonTextToolsConfig.LEVENSHTEIN_ENABLED) {
            plans.addAll(getLevenshteinPlans());
        }

        if (CommonTextToolsConfig.NGRAM_ENABLED) {
            plans.addAll(getNgramPlans());
        }

        if (CommonTextToolsConfig.SEWORDSIM_ENABLED) {
            plans.addAll(getSEWordSimPlans(Path.of("data", "sewordsim.sqlite")));
        }

        if (CommonTextToolsConfig.GLOVE_ENABLED) {
            plans.addAll(getGlovePlans(Path.of("data", "glove_cc_840B_300d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_wikigiga_300d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_wikigiga_200d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_wikigiga_100d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_wikigiga_50d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_twitter_200d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_twitter_100d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_twitter_50d.sqlite")));
            plans.addAll(getGlovePlans(Path.of("data", "glove_twitter_25d.sqlite")));
        }

        if (CommonTextToolsConfig.FASTTEXT_ENABLED) {
            plans.addAll(getFastTextPlans(Path.of("data", "cc.en.300.bin")));
            plans.addAll(getFastTextPlans(Path.of("data", "crawl-300d-2M-subword.bin")));
            plans.addAll(getFastTextPlans(Path.of("data", "wiki-news-300d-1M-subword.bin")));
        }

        if (CommonTextToolsConfig.WORDNET_ENABLED) {
            plans.addAll(getWordNetPlans(Path.of("data", "wordNet_dict/")));
        }

        if (CommonTextToolsConfig.NASARI_ENABLED) {
            plans.addAll(getNasariPlans(Path.of("data", "nasari_embed_english.sqlite")));
            plans.addAll(getNasariPlans(Path.of("data", "nasari_embed_umbc.sqlite")));
        }

        return plans;
    }

    private static List<EvalPlan> getJaroWinklerPlans() {
        var plans = new ArrayList<EvalPlan>();

        for (int t = 0; t <= 100; t += 5) {
            double threshold = t / 100.0;
            plans.add(new EvalPlan("jaroWinkler", Baseline.FIRST, t, new JaroWinklerMeasure(threshold)));
        }

        return plans;
    }

    private static List<EvalPlan> getLevenshteinPlans() {
        var plans = new ArrayList<EvalPlan>();

        for (int t = 0; t <= 100; t += 5) {
            double threshold = t / 100.0;
            for (int minLength = 0; minLength < 13; minLength++) {
                for (int maxDistance = 0; maxDistance < 13; maxDistance++) {
                    var group = String.format("levenshtein_%sL_%sD", minLength, maxDistance);
                    plans.add(new EvalPlan(group, Baseline.FIRST, t, new LevenshteinMeasure(minLength, maxDistance, threshold)));
                }
            }
        }

        return plans;
    }

    private static List<EvalPlan> getNgramPlans() {
        var plans = new ArrayList<EvalPlan>();

        for (Baseline b : Baseline.values()) {
            for (int t = 0; t <= 100; t += 5) {
                double threshold = t / 100.0;

                for (int n = 2; n <= 10; n++) {
                    var group = String.format("ngram_n%s", n);
                    plans.add(new EvalPlan(group, b, t, new NgramMeasure(NgramMeasure.Variant.LUCENE, n, threshold)));
                }
            }
        }

        return plans;
    }

    private static List<EvalPlan> getGlovePlans(Path dataSourcePath) throws SQLException {
        var dbName = dataSourcePath.getFileName().toString();
        var plans = new ArrayList<EvalPlan>();
        if (!Files.exists(dataSourcePath)) {
            LOGGER.info("Skipping {} because file does not exist.", dbName);
            return plans;
        }
        var db = new VectorSqliteDatabase(dataSourcePath);

        for (Baseline b : Baseline.values()) {
            for (int t = 0; t <= 100; t += 5) {
                double threshold = t / 100.0;

                plans.add(new EvalPlan("glove_" + dbName, b, t, new GloveMeasure(db, threshold)));
            }
        }

        return plans;
    }

    private static List<EvalPlan> getWordNetPlans(Path wordNetPath) throws IOException {
        var plans = new ArrayList<EvalPlan>();
        if (!Files.exists(wordNetPath)) {
            LOGGER.info("Skipping wordNet because dir does not exist: {}", wordNetPath);
            return plans;
        }
        ILexicalDatabase wordNetDB = null;
        WS4JConfiguration.getInstance().setCache(true);
        WS4JConfiguration.getInstance().setStem(true);

        Path wordNetDirPath = Path.of(CommonTextToolsConfig.WORDNET_DATA_DIR_PATH);
        IRAMDictionary dictionary = new RAMDictionary(wordNetDirPath.toFile(), ILoadPolicy.BACKGROUND_LOAD);
        dictionary.open();

        wordNetDB = new MITWordNet(dictionary);

        for (Baseline b : Baseline.values()) {
            for (int t = 0; t <= 100; t += 5) {
                double threshold = t / 100.0;

                plans.add(new EvalPlan("wordNet_LC", b, t, new WordNetMeasure(Map.of(new LeacockChodorow(wordNetDB), threshold))));
                plans.add(new EvalPlan("wordNet_JC", b, t, new WordNetMeasure(Map.of(new JiangConrath(wordNetDB), threshold))));
                plans.add(new EvalPlan("wordNet_Lesk", b, t, new WordNetMeasure(Map.of(new Lesk(wordNetDB), threshold))));
                plans.add(new EvalPlan("wordNet_Ezzi", b, t, new WordNetMeasure(Map.of(new Ezzikouri(wordNetDB), threshold))));
            }
        }

        return plans;
    }

    private static List<EvalPlan> getSEWordSimPlans(Path dataSourcePath) throws SQLException {
        var dbName = dataSourcePath.getFileName().toString();
        var plans = new ArrayList<EvalPlan>();
        if (!Files.exists(dataSourcePath)) {
            LOGGER.info("Skipping {} because file does not exist.", dbName);
            return plans;
        }
        var dataSource = new SEWordSimDataSource(dataSourcePath);

        for (Baseline b : Baseline.values()) {
            for (int t = 0; t <= 100; t += 5) {
                double threshold = t / 100.0;

                plans.add(new EvalPlan("sewsim_" + dbName, b, t, new SEWordSimMeasure(dataSource, threshold)));
            }
        }

        return plans;

    }

    private static List<EvalPlan> getFastTextPlans(Path dataSourcePath) throws SQLException {
        var dbName = dataSourcePath.getFileName().toString();
        var plans = new ArrayList<EvalPlan>();
        if (!Files.exists(dataSourcePath)) {
            LOGGER.info("Skipping {} because file does not exist.", dbName);
            return plans;
        }
        var dataSource = new DL4JFastTextDataSource(dataSourcePath);

        for (Baseline b : Baseline.values()) {
            for (int t = 0; t <= 100; t += 5) {
                double threshold = t / 100.0;

                plans.add(new EvalPlan("fastText_" + dbName, b, t, new FastTextMeasure(dataSource, threshold)));
            }
        }

        return plans;
    }

    private static List<EvalPlan> getNasariPlans(Path dataSourcePath) throws SQLException, IOException {
        var dbName = dataSourcePath.getFileName().toString();
        var plans = new ArrayList<EvalPlan>();
        if (!Files.exists(dataSourcePath)) {
            LOGGER.info("Skipping {} because file does not exist.", dbName);
            return plans;
        }
        var db = new VectorSqliteDatabase(dataSourcePath);
        var babelNet = new BabelNetDataSource(CommonTextToolsConfig.BABELNET_API_KEY, Path.of(CommonTextToolsConfig.BABELNET_CACHE_FILE_PATH));

        for (Baseline b : Baseline.values()) {
            for (int t = 0; t <= 100; t += 5) {
                double threshold = t / 100.0;

                plans.add(new EvalPlan("glove_" + dbName, b, t, new NasariMeasure(babelNet, db, threshold)));
            }
        }

        return plans;
    }

}
