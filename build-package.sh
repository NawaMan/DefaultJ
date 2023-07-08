#!/bin/bash

set -e

if [[ "$NAWAMAN_SIGNING_PASSWORD" == "" ]]; then
    echo "NAWAMAN_SIGNING_PASSWORD is not set."
    exit -1
fi

if [[ "$NAWAMAN_SONATYPE_PASSWORD" == "" ]]; then
    echo "NAWAMAN_SONATYPE_PASSWORD is not set."
    exit -1
fi

if [[ "$DEFAULTJ_KEYNAME" == "" ]]; then
    echo "DEFAULTJ_KEYNAME is not set."
    exit -1
fi

CURRENT_BRANCH=$(git branch --show-current)
SNAPSHOT='-SNAPSHOT'
if [[ "$CURRENT_BRANCH" == "release" ]]; then
    SNAPSHOT=""
fi

PROJECT_VERSION=$(cat .project-version-number)
PROJECT_BUILD=$(cat .project-build-number)
mvn versions:set -DnewVersion="$PROJECT_VERSION"."$PROJECT_BUILD""$SNAPSHOT"

./mvnw clean install package
