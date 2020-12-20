package ru.sstu.shopik.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sstu.shopik.domain.entities.Product;
import ru.sstu.shopik.domain.models.Pager;
import ru.sstu.shopik.services.CategoryService;
import ru.sstu.shopik.services.ProductService;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private static final int INITIAL_PAGE_SIZE = 18;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;


    @ModelAttribute
    public void addCurrentPage(Model model) {
        model.addAttribute("currentPage", "catalog");
    }

    @GetMapping
    public String getCatalogWithoutCategories(Model model, @PageableDefault(size = INITIAL_PAGE_SIZE) Pageable pageable,
                                              @RequestParam(required = false, value = "search") String search) {
        model.addAttribute("currentCategory", this.categoryService.getCatalog().orElse(null));
        pageable = isCorrectPage(pageable);
        Page<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.getAllByNameForSearchInGeneralCategory(search, pageable);
            model.addAttribute("search", search);
        } else {
            products = productService.getAll(pageable);
        }
        addInModel(model, products);
        return "catalog/catalog";
    }

    @GetMapping("/{category}")
    public String getCatalogWithCategories(Model model, @PathVariable String category,
                                           @PageableDefault(size = INITIAL_PAGE_SIZE) Pageable pageable,
                                           @RequestParam(required = false, value = "search") String search) {
        model.addAttribute("currentCategory", this.categoryService.getCategoryByEnCategory(category).orElse(null));
        pageable = isCorrectPage(pageable);
        Page<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.getAllByMotherCategoryAndProductName(category, search, pageable);
            model.addAttribute("search", search);
        } else {
            products = productService.getAllByMotherCategoryAndProductName(category, "", pageable);
        }
        addInModel(model, products);
        return "catalog/catalog";
    }

    @GetMapping("/{motherCategory}/{category}")
    public String getCatalogWithCategories(Model model, @PathVariable String motherCategory, @PathVariable String category,
                                           @PageableDefault(size = INITIAL_PAGE_SIZE) Pageable pageable,
                                           @RequestParam(required = false, value = "search") String search) {
        model.addAttribute("currentCategory", this.categoryService.getCategoryByEnCategory(category).orElse(null));
        pageable = isCorrectPage(pageable);
        Page<Product> products;
        if (search != null && !search.isEmpty()) {
            model.addAttribute("search", search);
            products = productService.getAllByCategoryAndProductName(category, search, motherCategory, pageable);
        } else {
            products = productService.getAllByCategoryAndProductName(category, "", motherCategory, pageable);
        }
        addInModel(model, products);
        return "catalog/catalog";
    }

    private Pageable isCorrectPage(Pageable pageable) {
        if (pageable.getPageSize() != INITIAL_PAGE_SIZE) {
            return PageRequest.of(0, INITIAL_PAGE_SIZE);
        } else {
            return pageable;
        }
    }

    private void addInModel(Model model, Page<Product> products) {
        Pager pager = new Pager(products.getTotalPages(), products.getNumber());
        model.addAttribute("products", products);
        model.addAttribute("pager", pager);
        model.addAttribute("catalog", categoryService.getCatalog().orElse(null));
    }

}
