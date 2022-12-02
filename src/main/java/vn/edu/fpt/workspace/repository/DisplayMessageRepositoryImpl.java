package vn.edu.fpt.workspace.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.workspace.constant.AppConstant;
import vn.edu.fpt.workspace.constant.ResponseStatusEnum;
import vn.edu.fpt.workspace.entity.DisplayMessage;
import vn.edu.fpt.workspace.exception.BusinessException;

import java.util.Objects;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 22:46
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public class DisplayMessageRepositoryImpl implements DisplayMessageRepository{

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public DisplayMessageRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<DisplayMessage> findByCodeAndLanguage(String code, String language) {
        if (Objects.isNull(code)){
            code = AppConstant.DEFAULT_CODE;
        }

        Object obj = redisTemplate.opsForValue().get(String.format("%s:%s", code, language));
        try {
            return Optional.of((DisplayMessage) obj);
        }catch (Exception ex){
            throw new BusinessException(ResponseStatusEnum.INTERNAL_SERVER_ERROR, "Can't cast redis data to display message: "+ ex.getMessage());
        }
    }
}
