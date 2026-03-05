package com.demo_hotel_service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MyController {
    @GetMapping("/home")
    public String home(){
        return "home.html";
    }

    @PostMapping("/logout")
    public void logout(){
        
    }
}



// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// import com.example.demo_hotel_service.models.Image;
// import com.example.demo_hotel_service.models.RoomModel;

// @Controller
// public class MyController {

//     @GetMapping("/")
//     public String home(Model model){


//         List<RoomModel> rooms = new ArrayList<>(List.of(
//             new RoomModel(
//                 1,
//                 250,
//                 "Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugiat eveniet quisquam veniam vero consequuntur quam accusamus molestias fugit consequatur voluptas, vitae nulla perferendis commodi? Natus id deleniti maiores cumque accusantium.\r\n" + //
//                 "Nemo, enim commodi cum, dignissimos vero beatae impedit laboriosam aspernatur dolorem distinctio natus ad laborum adipisci qui sequi incidunt ullam non veritatis et voluptatem. Aperiam ullam impedit dignissimos consequatur reiciendis.",
//                 "Lux",
//                 2,
//                 List.of("Wi-Fi", "ANY", "ANY", "ANY", "ANY"),
//                 List.of(
//                     new Image(1, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(2, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(3, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2")
//                 )
//             ),
//             new RoomModel(
//                 2,
//                 150,
//                 "Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugiat eveniet quisquam veniam vero consequuntur quam accusamus molestias fugit consequatur voluptas, vitae nulla perferendis commodi? Natus id deleniti maiores cumque accusantium.\r\n" + //
//                 "Nemo, enim commodi cum, dignissimos vero beatae impedit laboriosam aspernatur dolorem distinctio natus ad laborum adipisci qui sequi incidunt ullam non veritatis et voluptatem. Aperiam ullam impedit dignissimos consequatur reiciendis.",
//                 "Standart",
//                 2,
//                 List.of("Wi-Fi", "ANY", "ANY", "ANY", "ANY"),
//                 List.of(
//                     new Image(1, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(2, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(3, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2")
//                 )
//             ),
//             new RoomModel(
//                 3,
//                 80,
//                 "Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugiat eveniet quisquam veniam vero consequuntur quam accusamus molestias fugit consequatur voluptas, vitae nulla perferendis commodi? Natus id deleniti maiores cumque accusantium.\r\n" + //
//                 "Nemo, enim commodi cum, dignissimos vero beatae impedit laboriosam aspernatur dolorem distinctio natus ad laborum adipisci qui sequi incidunt ullam non veritatis et voluptatem. Aperiam ullam impedit dignissimos consequatur reiciendis.",
//                 "Econom",
//                 2,
//                 List.of("Wi-Fi", "ANY", "ANY", "ANY", "ANY"),
//                 List.of(
//                     new Image(1, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(2, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(3, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2")
//                 )
//             ),
//             new RoomModel(
//                 4,
//                 80,
//                 "Lorem ipsum dolor sit amet consectetur adipisicing elit. Fugiat eveniet quisquam veniam vero consequuntur quam accusamus molestias fugit consequatur voluptas, vitae nulla perferendis commodi? Natus id deleniti maiores cumque accusantium.\r\n" + //
//                 "Nemo, enim commodi cum, dignissimos vero beatae impedit laboriosam aspernatur dolorem distinctio natus ad laborum adipisci qui sequi incidunt ullam non veritatis et voluptatem. Aperiam ullam impedit dignissimos consequatur reiciendis.",
//                 "Any",
//                 2,
//                 List.of("Wi-Fi", "ANY", "ANY", "ANY", "ANY"),
//                 List.of(
//                     new Image(1, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(2, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2"),
//                     new Image(3, "http://example.com/this/is/a/very/long/path?param1=value1?param2=value2")
//                 )
//             )
//         ));

//         model.addAttribute("name", "Sanya");
//         model.addAttribute("rooms", rooms);
//         model.addAttribute("roomFormData", rooms.get(0));

//         return "home";
//     }

//     @PostMapping("/")
//     public ResponseEntity<?> create(@RequestBody RoomModel room){
//         return ResponseEntity.ok(room);
//     }
// }
