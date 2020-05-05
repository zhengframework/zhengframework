mvn clean site:site
mvn resources:resources
git add -R docs
git commit -m "Generated docs"
git push
