#!/bin/bash

set -e

function main() {
    COMMAND="$1"
    shift || true
    
    if [[ "$COMMAND" == ""        ]]; then build-full    "$@"; exit 0; fi
    if [[ "$COMMAND" == "quick"   ]]; then build-quick   "$@"; exit 0; fi
    if [[ "$COMMAND" == "test"    ]]; then build-test    "$@"; exit 0; fi
    if [[ "$COMMAND" == "package" ]]; then build-package "$@"; exit 0; fi
    if [[ "$COMMAND" == "release" ]]; then build-release "$@"; exit 0; fi
    if [[ "$COMMAND" == "help"    ]]; then show-help;          exit 0; fi
    
    echo "Unknown command: $COMMAND"
    show-help
    exit 1
}

function build-quick() {
    set-version
    ./mvnw clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dmaven.source.skip=true
}

function build-test() {
    set-version
    ./mvnw clean compile test -Dmaven.javadoc.skip=true -Dmaven.source.skip=true
}

function build-full() {
    set-version
    ./mvnw clean install
}

function build-package() {
    ensure-variable NAWAMAN_SIGNING_PASSWORD
    ensure-variable NAWAMAN_SONATYPE_PASSWORD
    
    KEY_VAR_NAME=$(cat key-var-name)
    ensure-variable "$KEY_VAR_NAME"
    
    set-version
    ./mvnw clean install package deploy -Dnexus.staging.skip=true
}

function build-release() {
    ensure-release
    
    ensure-variable NAWAMAN_SIGNING_PASSWORD
    ensure-variable NAWAMAN_SONATYPE_PASSWORD
    
    KEY_VAR_NAME=$(cat key-var-name)
    ensure-variable "$KEY_VAR_NAME"
    
    set-version
    ./mvnw clean install package deploy
}

function show-help() {
    echo "Build this project."
    echo "Commands"
    echo "  ''     : Full build with all the tests."
    echo "  quick  : Quick build skipping tests."
    echo "  test   : Compile and test."
    echo "  package: Compile, test, install and package (signed)."
    echo "  release: Build and release. Must be run while on 'release' branch only."
    echo "  help   : Show this message."
    echo "All command requires the follow files"
    echo "  project-version-number: Contains the major and minor version. For example: 2.0 for '2.0.6' version."
    echo "  project-build-number  : Contains the build number. For example: 6 for '2.0.6' version."
    echo "  key-var-name          : Contains the environmental variable name that holds the key name."
    echo "Release comand requires the following environmental variable."
    echo "  NAWAMAN_SIGNING_PASSWORD : The password for the signing key. Make sure the user name is in '~/.m2/settings.xml'".
    echo "  NAWAMAN_SONATYPE_PASSWORD: The password for SONATYPE account."
    exit 0
}

function ensure-variable() {
    local VAR_NAME=$1
    local VAR_VALUE=${!VAR_NAME}
    if [[ "$VAR_NAME" == "" ]]; then
        echo "$VAR_VALUE is not set."
        exit -1
    fi
}

function set-version() {
    if [[ ! -f project-version-number ]]; then
        echo "The file 'project-version-number' does not exists or not accessible."
        show-help
    fi
    if [[ ! -f project-build-number ]]; then
        echo "The file 'project-build-number' does not exists or not accessible."
        show-help
    fi
    
    local CURRENT_BRANCH=$(git branch --show-current)
    local SNAPSHOT='-SNAPSHOT'
    if [[ "$CURRENT_BRANCH" == "release" ]]; then
        SNAPSHOT=""
    fi
    
    local PROJECT_VERSION=$(cat project-version-number)
    local PROJECT_BUILD=$(cat project-build-number)
    mvn versions:set -DnewVersion="$PROJECT_VERSION"."$PROJECT_BUILD""$SNAPSHOT"
}

function ensure-release() {
    local CURRENT_BRANCH=$(git branch --show-current)
    if [[ "$CURRENT_BRANCH" != "release" ]]; then
        echo "You are not on the release branch!"
        echo "Publish only allow on the release branch."
        exit -1
    fi
}

main "$@"
