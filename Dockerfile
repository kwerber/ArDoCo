FROM maven

ADD ./* /ardoco/

VOLUME /ardoco/tests/results/

VOLUME /ardoco/tests/data/

WORKDIR /ardoco/

CMD mvn -DfailIfNoTests=false -Dtest=ExhaustiveEvaluation test
