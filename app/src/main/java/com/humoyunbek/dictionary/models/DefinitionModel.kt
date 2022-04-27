package com.humoyunbek.dictionary.models

class DefinitionModel {
    var definition:String? = null
    var example:String? = null

    constructor(definition: String?, example: String?) {
        this.definition = definition
        this.example = example
    }
}