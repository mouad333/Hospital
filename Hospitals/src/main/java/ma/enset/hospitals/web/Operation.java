package ma.enset.hospitals.web;

public @interface Operation  {
    String tag () default  "";
    String summary () default  "";
    String description() default  "";


}
