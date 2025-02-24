package com.SEB.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(namespace = "http://www.gesmes.org/xml/2002-08-01", localName = "Envelope")
public class ExchangeRateEnvelope {
    @JacksonXmlProperty(namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", localName = "Cube")
    private Cube cube;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cube {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "Cube")
        private List<DateCube> dateCubes;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DateCube {
            @JacksonXmlProperty(isAttribute = true)
            private String time;

            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "Cube")
            private List<CurrencyCube> currencyCubes;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CurrencyCube {
            @JacksonXmlProperty(isAttribute = true)
            private String currency;

            @JacksonXmlProperty(isAttribute = true)
            private String rate;
        }
    }
}
