package apiauthuser.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import apiauthuser.services.UtilsService;

@Service
public class UtilsServiceImpl implements UtilsService {
    
    public String createUrlGetAllCoursesByUser(UUID userId, Pageable pageable) {
        return "/courses/getAllCoursesPageable?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
    }
}
