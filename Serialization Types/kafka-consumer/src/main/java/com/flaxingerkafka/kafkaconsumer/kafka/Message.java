package com.flaxingerkafka.kafkaconsumer.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(@JsonProperty("message") String Message,
                      @JsonProperty("identifier") int identifier) {}

