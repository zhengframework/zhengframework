newVersion=$1
echo "use new version: $newVersion"
mvn versions:set-property -Dproperty=revision -DnewVersion="$newVersion"