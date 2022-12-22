package com.apptasticsoftware.tickersymbol;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("java:S5976")
class TickerSymbolSearchTest {
    private static final int CACHE_SIZE = 10;

    private final TickerSymbolSearch searchIdentifiersOnly = new TickerSymbolSearch(CACHE_SIZE) {
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

    private final TickerSymbolSearch searchIsinOnly = new TickerSymbolSearch(CACHE_SIZE) {
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

    private final TickerSymbolSearch searchCusipOnly = new TickerSymbolSearch(CACHE_SIZE) {
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

    private final TickerSymbolSearch searchSedolOnly = new TickerSymbolSearch(CACHE_SIZE) {
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


    private final TickerSymbolSearch searchNotIdentifiers = new TickerSymbolSearch(CACHE_SIZE) {
        @Override
        List<TickerSymbol> searchByIdentifiers(String identifier) {
            return Collections.emptyList();
        }

    };

    @Test
    void searchByIsin() {
        var search = new TickerSymbolSearch(CACHE_SIZE);
        var list = search.searchByIdentifier("SE0000825820");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchByCusip() {
        var search = TickerSymbolSearch.getInstance();
        var list = search.searchByIdentifier("38259P508");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Test
    void searchBySedol() {
        var search = TickerSymbolSearch.getInstance(49);
        var list = search.searchByIdentifier("2000019");
        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getSymbol());
        assertNotNull(list.get(0).getDescription());
    }

    @Disabled("Run manually")
    @SuppressWarnings("java:S2925")
    @Test
    void perfTest() throws InterruptedException {
        var identifiers = List.of("CA09228F1036", "CA82509L1076", "US67066G1040", "US0079031078", "US2546871060",
                "US64110L1061", "US0846707026", "US0378331005", "US4581401001", "US9344231041", "US57636Q1040",
                "US00724F1012", "US92826C8394", "US88160R1014", "US6541061031", "US8356993076", "US8552441094",
                "US5949181045", "US7134481081", "US3453708600", "US9311421039", "US46625H1005", "US7170811035",
                "US5801351017", "US0970231058", "US5398301094", "US0605051046", "US0258161092", "US7475251036",
                "US87612E1064", "US17275R1023", "US11135F1012", "US1491231015", "US2855121099", "US68389X1054",
                "US92343V1044", "US38141G1040", "US6174464486", "US4370761029", "US30303M1027", "US36467W1099",
                "US00165C1045", "US67421J1088", "US7710491033",
                "US02079K1079", "US0231351067", "SE0000108656",
                "023135106", "38259P508",
                "2000019", "BH4HKS3", "BMDGCK2"
                );

        int count = 1;
        long totalResponseTime = 0;
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long wait = 2 * 1000;
        int times = 3;

        for (int i = 0; i < times; ++i) {
            for (String id : identifiers) {
                System.out.println("ID: " + id + ", attempt: " + count + " of " + identifiers.size() * times);
                long timestamp1 = System.currentTimeMillis();

                var list = searchNotIdentifiers.searchByIdentifier(id);
                assertFalse(list.isEmpty());
                assertNotNull(list.get(0).getSymbol());
                assertNotNull(list.get(0).getDescription());

                long timestamp2 = System.currentTimeMillis();
                long responseTime = timestamp2 - timestamp1;
                totalResponseTime += responseTime;
                minTime = Math.min(minTime, responseTime);
                maxTime = Math.max(maxTime, responseTime);
                System.out.println("Response time: " + responseTime + ", Avg: " + (totalResponseTime/count) + ", Min: " + minTime + ", Max: " + maxTime);
                count++;

                TimeUnit.MICROSECONDS.sleep(wait);
            }
        }

        System.out.println("Done!");
    }

    @Disabled("Run manually")
    @SuppressWarnings("java:S2925")
    @Test
    void perfTestParallel() throws InterruptedException {
        var identifiers = List.of("CA09228F1036", "CA82509L1076", "US67066G1040", "US0079031078", "US2546871060",
                "US64110L1061", "US0846707026", "US0378331005", "US4581401001", "US9344231041", "US57636Q1040",
                "US00724F1012", "US92826C8394", "US88160R1014", "US6541061031", "US8356993076", "US8552441094",
                "US5949181045", "US7134481081", "US3453708600", "US9311421039", "US46625H1005", "US7170811035",
                "US5801351017", "US0970231058", "US5398301094", "US0605051046", "US0258161092", "US7475251036",
                "US87612E1064", "US17275R1023", "US11135F1012", "US1491231015", "US2855121099", "US68389X1054",
                "US92343V1044", "US38141G1040", "US6174464486", "US4370761029", "US30303M1027", "US36467W1099",
                "US00165C1045", "US67421J1088", "US7710491033",
                "US02079K1079", "US0231351067", "SE0000108656",
                "023135106", "38259P508",
                "2000019", "BH4HKS3", "BMDGCK2"
        );

        var first = searchNotIdentifiers.searchByIdentifier(identifiers.get(0));
        assertFalse(first.isEmpty());
        assertNotNull(first.get(0).getSymbol());
        assertNotNull(first.get(0).getDescription());

        TimeUnit.MICROSECONDS.sleep(3000);
        System.out.println("Start");

        identifiers.stream().parallel()
                .forEach(id ->  {
                    var list = searchNotIdentifiers.searchByIdentifier(id);
                    assertFalse(list.isEmpty());
                    assertNotNull(list.get(0).getSymbol());
                    assertNotNull(list.get(0).getDescription());
                });

        System.out.println("Done!");
    }

    @SuppressWarnings("java:S2925")
    @Test
    void pendingRequests() {
        var identifiers = List.of("CA09228F1036", "CA09228F1036", "CA09228F1036", "CA09228F1036", "CA09228F1036",
                "CA09228F1036", "CA09228F1036", "CA09228F1036", "CA09228F1036", "CA09228F1036", "CA09228F1036"
        );

        var search = TickerSymbolSearch.getInstance(48);
        identifiers.stream().parallel()
                .forEach(id ->  {
                    var list = search.searchByIdentifier(id);
                    assertFalse(list.isEmpty());
                    assertNotNull(list.get(0).getSymbol());
                    assertNotNull(list.get(0).getDescription());
                });
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
