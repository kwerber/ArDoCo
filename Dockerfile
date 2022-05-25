FROM maven

ADD ./* /ardoco/

VOLUME /ardoco/tests/results/

WORKDIR /ardoco/

CMD mvn -DfailIfNoTests=false -Dtest=ExhaustiveEvaluation test
