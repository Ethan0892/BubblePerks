#!/bin/bash

echo "Building BubblePerks..."
mvn clean package

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful! JAR file is located in the target directory."
    echo ""
    JAR_FILE=$(find target -name "BubblePerks-*.jar" -type f 2>/dev/null | head -1)
    if [ -n "$JAR_FILE" ]; then
        echo "JAR location: $JAR_FILE"
    fi
else
    echo ""
    echo "Build failed! Check the output above for errors."
    exit 1
fi
