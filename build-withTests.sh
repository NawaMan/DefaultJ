#!/bin/bash

set -e

CURRENT_BRANCH=$(git branch --show-current)
SNAPSHOT='-SNAPSHOT'
if [[ "$CURRENT_BRANCH" == "release" ]]; then
    SNAPSHOT=""
fi

PROJECT_VERSION=$(cat .project-version-number)
PROJECT_BUILD=$(cat .project-build-number)
mvn versions:set -DnewVersion="$PROJECT_VERSION"."$PROJECT_BUILD""$SNAPSHOT"

./mvnw clean install
