package me.gamercoder215.kotatime.storage

import me.gamercoder215.kotatime.util.asDate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestNetwork {

    @Test
    @DisplayName("Test #getStatFilters")
    fun testGetStatFilters() {
        val list1 = listOf(
            "2021-01",
            "2021-02",
            "2021-03",
            "2021-04",
        )
        Assertions.assertEquals(list1, getStatFilters("2021-01-02T00:00:00Z".asDate, "2021-04-05T00:00:00Z".asDate))

        val list2 = listOf(
            "2021-05", "2021-06", "2021-07", "2021-08", "2021-09", "2021-10", "2021-11", "2021-12",
            "2022-01", "2022-02", "2022-03", "2022-04", "2022-05", "2022-06", "2022-07", "2022-08", "2022-09", "2022-10", "2022-11", "2022-12",
            "2023-01", "2023-02", "2023-03", "2023-04", "2023-05", "2023-06", "2023-07", "2023-08", "2023-09", "2023-10", "2023-11", "2023-12",
            "2024-01", "2024-02", "2024-03", "2024-04", "2024-05", "2024-06", "2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12",
            "2025-01", "2025-02", "2025-03", "2025-04", "2025-05", "2025-06", "2025-07", "2025-08", "2025-09", "2025-10", "2025-11", "2025-12",
            "2026-01", "2026-02", "2026-03", "2026-04", "2026-05", "2026-06", "2026-07", "2026-08"
        )
        Assertions.assertEquals(list2, getStatFilters("2021-05-13T12:00:00Z".asDate, "2026-08-12T00:12:13Z".asDate))

        val list3 = listOf(
            "2021-12",
            "2022-01",
            "2022-02",
        )
        Assertions.assertEquals(list3, getStatFilters("2021-12-01T09:00:05Z".asDate, "2022-02-01T17:06:00Z".asDate))

        val list4 = listOf(
            "2022-05",
            "2022-06",
            "2022-07"
        )
        Assertions.assertEquals(list4, getStatFilters("2022-05-01T13:17:59Z".asDate, "2022-07-11T23:59:59Z".asDate))

        val list5 = listOf(
            "2023-01",
            "2023-02"
        )
        Assertions.assertEquals(list5, getStatFilters("2023-01-31T23:59:59Z".asDate, "2023-02-28T23:59:59Z".asDate))
    }

}