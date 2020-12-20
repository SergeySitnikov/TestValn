package ru.sstu.shopik.controllers.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.sstu.shopik.domain.entities.Category;
import ru.sstu.shopik.exceptions.CategoryDoesNotExist;
import ru.sstu.shopik.exceptions.UserDoesNotExist;
import ru.sstu.shopik.forms.ProductAddForm;
import ru.sstu.shopik.forms.validators.ProductAddFormValidator;
import ru.sstu.shopik.services.CategoryService;
import ru.sstu.shopik.services.ProductService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("profile/products/addProduct")
public class AddProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductAddFormValidator productValidators;

    @InitBinder("productAddForm")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(this.productValidators);
    }

    @ModelAttribute
    public void addCurrentPage(Model model) {
        model.addAttribute("currentSection", "products");
    }

    @PostMapping
    public String newProduct(Model model, Authentication authentication, @Valid @ModelAttribute("productAddForm") ProductAddForm productAddForm, BindingResult binding) {
        if (binding.hasErrors()) {
            model.addAttribute("catalog", categoryService.getCatalog().orElse(null));
            return "profile/addProduct";
        }
        try {
            this.productService.createProductFromAddProductForm(productAddForm, authentication);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        } catch (UserDoesNotExist e){

        }
        return "redirect:/profile/products/addProduct";
    }

    @GetMapping
    public String addProduct(Model model, ProductAddForm productAddForm) {
        model.addAttribute("productAddForm", productAddForm);
        model.addAttribute("catalog", categoryService.getCatalog().orElse(null));
        return "profile/addProduct";
    }

    @RequestMapping("/categoryMain")
    @ResponseBody
    public List<Category> getCatalog(@RequestParam("name") String name, Model model) {
        try {
            return categoryService.getSubCategories(name);
        } catch (CategoryDoesNotExist ex) {
            return null;
        }
    }
}
