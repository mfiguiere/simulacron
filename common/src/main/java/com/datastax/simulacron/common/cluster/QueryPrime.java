package com.datastax.simulacron.common.cluster;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by gregbestland on 4/3/17. */
public final class QueryPrime {
  public final When when;
  public final Then then;

  @JsonCreator
  public QueryPrime(@JsonProperty("when") When when, @JsonProperty("then") Then then) {
    this.when = when;
    this.then = then;
  }

  public static final class When {
    public final String query;
    public final String[] consistency;

    @JsonCreator
    public When(
        @JsonProperty("query") String query, @JsonProperty("consistency") String[] consistency) {
      this.query = query;
      this.consistency = consistency;
    }
  }

  public static final class Then {
    public final Row rows[];
    public final String result;
    public final Column_types column_types;

    @JsonCreator
    public Then(
        @JsonProperty("rows") Row[] rows,
        @JsonProperty("result") String result,
        @JsonProperty("column_types") Column_types column_types) {
      this.rows = rows;
      this.result = result;
      this.column_types = column_types;
    }

    public static final class Row {

      @JsonCreator
      public Row() {}
    }

    public static final class Column_types {

      @JsonCreator
      public Column_types() {}
    }
  }
}