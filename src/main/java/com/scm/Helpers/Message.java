package com.scm.Helpers;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  private String content ;

   private  MessageType messageColorType ;

}
