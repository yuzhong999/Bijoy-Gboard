package com.example

import com.example.engine.BijoyCharacterMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testBijoyConsonantMappings() {
        assertEquals("ক", BijoyCharacterMapper.getOutput('j', false))
        assertEquals("খ", BijoyCharacterMapper.getOutput('j', true))
        assertEquals("ত", BijoyCharacterMapper.getOutput('k', false))
        assertEquals("থ", BijoyCharacterMapper.getOutput('k', true))
        assertEquals("ব", BijoyCharacterMapper.getOutput('h', false))
        assertEquals("ভ", BijoyCharacterMapper.getOutput('h', true))
        assertEquals("ম", BijoyCharacterMapper.getOutput('m', false))
        assertEquals("শ", BijoyCharacterMapper.getOutput('m', true))
        assertEquals("র", BijoyCharacterMapper.getOutput('v', false))
        assertEquals("ল", BijoyCharacterMapper.getOutput('v', true))
        assertEquals("স", BijoyCharacterMapper.getOutput('n', false))
        assertEquals("ষ", BijoyCharacterMapper.getOutput('n', true))
    }

    @Test
    fun testBijoyVowelSigns() {
        assertEquals("া", BijoyCharacterMapper.getOutput('f', false))
        assertEquals("অ", BijoyCharacterMapper.getOutput('f', true))
        assertEquals("ি", BijoyCharacterMapper.getOutput('d', false))
        assertEquals("ী", BijoyCharacterMapper.getOutput('d', true))
        assertEquals("ু", BijoyCharacterMapper.getOutput('s', false))
        assertEquals("ূ", BijoyCharacterMapper.getOutput('s', true))
        assertEquals("ৃ", BijoyCharacterMapper.getOutput('a', false))
        assertEquals("ঋ", BijoyCharacterMapper.getOutput('a', true))
        assertEquals("ে", BijoyCharacterMapper.getOutput('c', false))
        assertEquals("ৈ", BijoyCharacterMapper.getOutput('c', true))
        assertEquals("ো", BijoyCharacterMapper.getOutput('x', false))
        assertEquals("ৌ", BijoyCharacterMapper.getOutput('x', true))
    }

    @Test
    fun testBijoyNumeralsAndSymbols() {
        assertEquals("১", BijoyCharacterMapper.getOutput('1', false))
        assertEquals("০", BijoyCharacterMapper.getOutput('0', false))
        assertEquals("৳", BijoyCharacterMapper.getOutput('4', true))
        assertEquals("্", BijoyCharacterMapper.getOutput('g', false))
        assertEquals("ং", BijoyCharacterMapper.getOutput('q', true))
        assertEquals("ৎ", BijoyCharacterMapper.getOutput(',', true))
        assertEquals("ঁ", BijoyCharacterMapper.getOutput('.', true))
    }
}
