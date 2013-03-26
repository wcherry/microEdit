package org.sss.micro.grammars

class Grammar {
    List fileTypes
    String foldingStartMarker
    String foldingStopMarker
    String keyEquivalent
    String name
    List patterns
    Map repository
    Object storage_modifiers
    String scopeName
    String uuid

    Map snippets = [:]

    void addSnippet(String trigger, Snippet snippet) {
        snippets[trigger] = snippet
    }

    public String toString() {
        name ?: "[No Name]"
    }

}