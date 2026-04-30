/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer.wifi.scanner

import android.net.wifi.ScanResult
import com.vrem.annotation.OpenClass
import com.vrem.util.ssid

data class WiFiScanHistoryRow(
    val scanIndex: Int,
    val scanTimeMillis: Long,
    val bssid: String,
    val ssid: String,
    val level: Int,
    val frequency: Int,
    val centerFrequency0: Int,
    val centerFrequency1: Int,
    val channelWidth: Int,
    val capabilities: String,
    val scanResultTimestampMicros: Long,
)

@OpenClass
internal class WiFiScanHistory {
    private val rows: MutableList<WiFiScanHistoryRow> = mutableListOf()
    private var scanIndex: Int = 0

    @Synchronized
    fun add(scanResults: List<ScanResult>) {
        if (scanResults.isEmpty()) return
        scanIndex++
        val scanTimeMillis = System.currentTimeMillis()
        rows.addAll(
            scanResults.map {
                WiFiScanHistoryRow(
                    scanIndex,
                    scanTimeMillis,
                    it.BSSID.orEmpty(),
                    it.ssid(),
                    it.level,
                    it.frequency,
                    it.centerFreq0,
                    it.centerFreq1,
                    it.channelWidth,
                    it.capabilities.orEmpty(),
                    it.timestamp,
                )
            },
        )
    }

    @Synchronized
    fun rows(): List<WiFiScanHistoryRow> = rows.toList()

    @Synchronized
    fun clear() {
        rows.clear()
        scanIndex = 0
    }
}
