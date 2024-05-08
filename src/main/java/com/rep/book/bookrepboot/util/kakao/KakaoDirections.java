package com.rep.book.bookrepboot.util.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class KakaoDirections {
  private List<Route> routes;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  public static class Route {
    private List<Section> sections;
    private Summary summary;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class Section {
      private List<Road> roads;

      @JsonIgnoreProperties(ignoreUnknown = true)
      @Getter
      public static class Road {
        private List<Double> vertexes;
      }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    public static class Summary {
      private Fare fare;// 요금 정보
      private Integer distance;// 전체 검색 결과 거리(미터)
      private Integer duration;// 목적지까지 소요 시간(초)

      @JsonIgnoreProperties(ignoreUnknown = true)
      @Getter
      public static class Fare {
        private Integer taxi;// 택시 요금(원)
        private Integer toll;// 통행 요금(원)
      }
    }
  }
}

