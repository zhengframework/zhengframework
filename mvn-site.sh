mvn -P extra-report,javadoc8 clean compile site:site
rm -rf docs/*
mvn resources:resources
git add docs
git commit -m "Generated docs"