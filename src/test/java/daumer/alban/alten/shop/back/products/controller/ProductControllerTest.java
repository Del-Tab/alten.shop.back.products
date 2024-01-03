package daumer.alban.alten.shop.back.products.controller;

import daumer.alban.alten.shop.back.products.enums.CategoryEnum;
import daumer.alban.alten.shop.back.products.enums.InventoryStatusEnum;
import daumer.alban.alten.shop.back.products.model.Product;
import daumer.alban.alten.shop.back.products.repository.ProductRepository;
import jakarta.servlet.ServletContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    public static final Long ID = 1L;
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final Long PRICE = 2L;
    public static final Long QUANTITY = 3L;
    public static final InventoryStatusEnum INVENTORY_STATUS_ENUM = InventoryStatusEnum.INSTOCK;
    public static final CategoryEnum CATEGORY_ENUM = CategoryEnum.Accessories;
    public static final String IMAGE = "image";
    public static final Long RATING = 4L;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private ProductRepository productRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(productRepository);
    }

    @Test
    public void givenWebApplicationContext_whenServletContext_thenItProvidesProductController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("productController"));
    }

    @Test
    public void givenNoProduct_whenRequestList_thenReturnsEmptyArrayJson() throws Exception {
        //nb this test is also covered by givenNProducts_whenRequestList_thenReturnsNSizedItemArrayJson(0)
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenOneProduct_whenRequestList_thenReturnsSingleItemArrayJson() throws Exception {
        when(productRepository.findAll()).thenReturn(List.of(new Product(
                ID, CODE,
                NAME,
                DESCRIPTION,
                PRICE,
                QUANTITY,
                INVENTORY_STATUS_ENUM,
                CATEGORY_ENUM, IMAGE, RATING)));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value(CODE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(PRICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(QUANTITY))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].inventoryStatus").value(INVENTORY_STATUS_ENUM.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(CATEGORY_ENUM.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].image").value(IMAGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rating").value(RATING))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "2", "5", "26", "42042"})
    public void givenNProducts_whenRequestList_thenReturnsNSizedItemArrayJson(int nbElements) throws Exception {
        when(productRepository.findAll()).thenReturn(Stream.generate(() -> new Product(
                ID, CODE,
                NAME,
                DESCRIPTION,
                PRICE,
                QUANTITY,
                INVENTORY_STATUS_ENUM,
                CATEGORY_ENUM, IMAGE, RATING)).limit(nbElements).collect(Collectors.toList()));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(nbElements)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", Matchers.everyItem(equalTo(ID.intValue()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].code", Matchers.everyItem(equalTo(CODE))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.everyItem(equalTo(NAME))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description", Matchers.everyItem(equalTo(DESCRIPTION))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].price", Matchers.everyItem(equalTo(PRICE.intValue()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].quantity", Matchers.everyItem(equalTo(QUANTITY.intValue()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].inventoryStatus", Matchers.everyItem(equalTo(INVENTORY_STATUS_ENUM.name()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].category", Matchers.everyItem(equalTo(CATEGORY_ENUM.name()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].image", Matchers.everyItem(equalTo(IMAGE))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].rating", Matchers.everyItem(equalTo(RATING.intValue()))))
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
    }
}