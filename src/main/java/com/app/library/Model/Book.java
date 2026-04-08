package com.app.library.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "books")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Setter
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Getter
    @Setter
    @Column(name = "author", nullable = false, length = 50)
    private String author;

    @Getter
    @Setter
    @Column(name = "publishDate",  nullable = false)
    private LocalDate publishDate;

    @Getter
    @Setter
    @Column(name = "cover_image_path", length = 255)
    private String coverImagePath;


}
