package com.rep.book.bookrepboot.dto;

/**
 * 노드  Class
 */
public class NodeDTO {
    private String email;
    private String address;
    private Double x;//경도
    private Double y;//위도
    /**
    *  조회
    * @return name
    */
    public String getEmail() {
        return this.email;
    }
   /**
    *  설정
    * @return name
    */
    public void setEmail(String email) {
        this.email = email;
    }
   /**
    *  조회
    * @return address
    */
    public String getAddress() {
        return this.address;
    }
   /**
    *  설정
    * @return address
    */
    public void setAddress(String address) {
        this.address = address;
    }
   /**
    * 경도 조회
    * @return x
    */
    public Double getX() {
        return this.x;
    }
   /**
    * 경도 설정
    * @return x
    */
    public void setX(Double x) {
        this.x = x;
    }
   /**
    * 위도 조회
    * @return y
    */
    public Double getY() {
        return this.y;
    }
   /**
    * 위도 설정
    * @return y
    */
    public void setY(Double y) {
        this.y = y;
    }

}