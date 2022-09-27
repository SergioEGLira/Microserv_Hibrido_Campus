package apiauthuser.dtos;

import java.util.UUID;

import apiauthuser.enums.CourseLevel;
import apiauthuser.enums.CourseStatus;
import lombok.Data;

@Data
public class CourseDto {

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private UUID userInstructor;
    private CourseLevel courseLevel;

}
