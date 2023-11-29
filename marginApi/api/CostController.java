package com.example.marginApi.api;

import com.example.marginApi.model.Cost;
import com.example.marginApi.service.CostService;
import com.example.marginApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("api/cost")
@RestController
@CrossOrigin(origins = "https://marginsite.ovh", allowCredentials = "true")
public class CostController {

    private final UserService userService;

    private final CostService costService;

    @Autowired
    public CostController(UserService userService, CostService costService) {
        this.userService = userService;
        this.costService = costService;
    }

    @PostMapping
    public void addCost(@RequestBody @Validated @NonNull Cost cost, @CookieValue(value = "uuid") String uuid){
        this.costService.addCost(this.userService.getUser(UUID.fromString(uuid)), cost);
    }
    @GetMapping
    public List<Cost> getAllCost(@CookieValue(value = "uuid") String uuid){
        return this.costService.getAllCosts(this.userService.getUser(UUID.fromString(uuid)));
    }
    @GetMapping(path = "/fixed")
    public List<Cost> getAllFixedCost(@CookieValue(value = "uuid") String uuid){
        return this.costService.getFixedCostList(this.userService.getUser(UUID.fromString(uuid)));
    }

    @GetMapping(path = "/variable")
    public List<Cost> getAllVariableCost(@CookieValue(value = "uuid") String uuid){
        return this.costService.getVariableCostList(this.userService.getUser(UUID.fromString(uuid)));
    }
    @GetMapping(path = "/{costId}")
    public Cost getCostById(@CookieValue(value = "uuid") String uuid, @PathVariable("costId") int costId){
        return this.costService.getCostByID(this.userService.getUser(UUID.fromString(uuid)), costId);
    }

    @DeleteMapping(path = "/{costId}")
    public void deleteCostById(@CookieValue(value = "uuid") String uuid, @PathVariable("costId") int costId ){
        this.costService.removeCost(this.userService.getUser(UUID.fromString(uuid)), costId);
    }

    @PutMapping
    public void updateCost(@CookieValue(value = "uuid") String uuid, @Validated @NonNull @RequestBody Cost cost){
        this.costService.updateCost(this.userService.getUser(UUID.fromString(uuid)), cost);
    }

    //ready for save number of sales by mouth
    //@GetMapping(path = "/numberOfSale")
    //public int getNumberOfSale(@CookieValue(value = "uuid") String uuid){
    //    return MainController.getInstance().getUser(UUID.fromString(uuid)).getNumberOfSale();
    //    return 0;
    //}
    //@PostMapping(path = "/numberOfSale")
    //public void postNumberOfSale(@CookieValue(value = "uuid") String uuid, @RequestBody NumberOfSale numberOfSale){
    //    MainController.getInstance().getUser(UUID.fromString(uuid)).setNumberOfSale(numberOfSale.getNumber());
    //}
}
