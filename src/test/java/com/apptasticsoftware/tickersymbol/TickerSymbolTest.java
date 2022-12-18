package com.apptasticsoftware.tickersymbol;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TickerSymbolTest {

    @Test
    void registrationAuthoritySimpleEqualsContract() {
        EqualsVerifier.simple().forClass(TickerSymbol.class).verify();
    }
}
