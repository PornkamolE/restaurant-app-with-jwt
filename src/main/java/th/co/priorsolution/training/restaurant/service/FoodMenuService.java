package th.co.priorsolution.training.restaurant.service;

import org.springframework.stereotype.Service;
import th.co.priorsolution.training.restaurant.entity.FoodMenuEntity;
import th.co.priorsolution.training.restaurant.model.ResponseModel;
import th.co.priorsolution.training.restaurant.repository.FoodMenuRepository;

import java.util.List;

@Service
public class FoodMenuService {

    private FoodMenuRepository foodMenuRepository;

    public FoodMenuService(FoodMenuRepository foodMenuRepository) {
        this.foodMenuRepository = foodMenuRepository;
    }

    public ResponseModel<List<FoodMenuEntity>> getAllMenu() {
        ResponseModel<List<FoodMenuEntity>> result = new ResponseModel<>();
        List<FoodMenuEntity> menu = foodMenuRepository.findAll();

        result.setStatus(200);
        result.setDescription("ok");
        result.setData(menu);

        return result;
    }
}
