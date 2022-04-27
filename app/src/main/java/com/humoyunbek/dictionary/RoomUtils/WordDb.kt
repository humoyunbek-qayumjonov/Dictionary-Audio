package com.humoyunbek.dictionary.RoomUtils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WordDb {
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
    var word:String? = null
    var definition:String? = null
    var example:String? = null
    var audioLink:String? = null
    var save:Boolean? = false
    var reading:String? = null

    constructor(
        word: String?,
        definition: String?,
        example: String?,
        audioLink: String?,
        save: Boolean?,
        reading:String?
    ) {
        this.word = word
        this.definition = definition
        this.example = example
        this.audioLink = audioLink
        this.save = save
        this.reading = reading
    }
}