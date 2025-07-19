
mvn clean install

if [ $? -ne 0 ]; then
  echo "Maven build failed. Exiting."
  exit 1
fi

java -cp ./target/rtree-1.0.2.jar TesterDouble $1
