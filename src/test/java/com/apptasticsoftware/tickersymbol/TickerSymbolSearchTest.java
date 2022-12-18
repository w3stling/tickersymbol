package com.apptasticsoftware.tickersymbol;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TickerSymbolSearchTest {
    private final TickerSymbolSearch searchIdentifiersOnly = new TickerSymbolSearch() {
        @Override
        List<TickerSymbol> searchByIsin(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchByCusip(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchBySedol(String identifier) {
            return Collections.emptyList();
        }
    };

    private final TickerSymbolSearch searchIsinOnly = new TickerSymbolSearch() {
        @Override
        List<TickerSymbol> searchByIdentifiers(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchByCusip(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchBySedol(String identifier) {
            return Collections.emptyList();
        }
    };

    private final TickerSymbolSearch searchCusipOnly = new TickerSymbolSearch() {
        @Override
        List<TickerSymbol> searchByIdentifiers(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchByIsin(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchBySedol(String identifier) {
            return Collections.emptyList();
        }
    };

    private final TickerSymbolSearch searchSedolOnly = new TickerSymbolSearch() {
        @Override
        List<TickerSymbol> searchByIdentifiers(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchByIsin(String identifier) {
            return Collections.emptyList();
        }

        @Override
        List<TickerSymbol> searchByCusip(String identifier) {
            return Collections.emptyList();
        }
    };

    @Test
    void searchByIsin() {
        var search = new TickerSymbolSearch();
        var list = search.searchByIdentifier("SE0000825820");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchByCusip() {
        var search = new TickerSymbolSearch();
        var list = search.searchByIdentifier("38259P508");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchBySedol() {
        var search = new TickerSymbolSearch();
        var list = search.searchByIdentifier("2000019");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }


    @Test
    void searchByIdentifierOnly() {
        var list = searchIdentifiersOnly.searchByIdentifier("US0231351067");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());

        list = searchIdentifiersOnly.searchByIdentifier("023135106");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());

        list = searchIdentifiersOnly.searchByIdentifier("2000019");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchByIsinOnly() {
        var list = searchIsinOnly.searchByIdentifier("US0231351067");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchByCusipOnly() {
        var list = searchCusipOnly.searchByIdentifier("023135106");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchBySedolOnly() {
        var list = searchSedolOnly.searchByIdentifier("2000019");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }
}
