#!/bin/bash

npm run build --prefix front
mkdir -p src/main/resources/static
find src/main/resources/static -type f ! -name "*.properties" -exec rm -f {} \;
cp -r front/build/* src/main/resources/static
