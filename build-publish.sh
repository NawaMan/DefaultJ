#!/bin/bash

set -e

CURRENT_BRANCH=$(git branch --show-current)
if [[ "$CURRENT_BRANCH" != "release" ]]; then
    echo "You are not on the release branch!"
    echo "Publish only allow on the release branch."
    exit -1
fi

if [[ "$NAWAMAN_SIGNING_PASSWORD" == "" ]]; then
    echo "NAWAMAN_SIGNING_PASSWORD is not set."
    exit -1
fi

if [[ "$NAWAMAN_SONATYPE_PASSWORD" == "" ]]; then
    echo "NAWAMAN_SONATYPE_PASSWORD is not set."
    exit -1
fi

if [[ "$NULLABLEJ_KEYNAME" == "" ]]; then
    echo "NULLABLEJ_KEYNAME is not set."
    exit -1
fi

PROJECT_VERSION=$(cat .project-version-number)
PROJECT_BUILD=$(cat .project-build-number)
mvn versions:set -DnewVersion="$PROJECT_VERSION"."$PROJECT_BUILD"

./mvnw clean install package deploy
