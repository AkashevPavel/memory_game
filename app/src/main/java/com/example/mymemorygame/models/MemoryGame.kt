package com.example.mymemorygame.models

import com.example.mymemorygame.util.DEFAULT_ICONS

class MemoryGame (private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map {MemoryCard(it)}
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]
        //3 cases
        // 0 cards are flipped over => simply flip a card
        // 1 card is flipped over => flip over a card & check if they are matched
        // 2 cards are flipped over => restore cards & flip the selected one
        // cases for 0 and 2 are equal except in case 0 'restore' wont do anything
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        }else{
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) return false
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true

    }

    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched) card.isFaceUp = false
        }
    }

    fun heveWonGame(): Boolean = numPairsFound == boardSize.getNumPairs()
    fun isCardFaceUp(position: Int): Boolean = cards[position].isFaceUp
    fun getNumMoves(): Int = numCardFlips / 2


}