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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    private static final Long ID = 1L;
    private static final Long ANOTHER_ID = 5L;
    private static final String CODE = "code";
    private static final String ANOTHER_CODE = "code2";
    private static final String NAME = "name";
    private static final String ANOTHER_NAME = "name2";
    private static final String DESCRIPTION = "description";
    private static final String ANOTHER_DESCRIPTION = "description2";
    private static final Long PRICE = 2L;
    private static final Long ANOTHER_PRICE = 6L;
    private static final Long QUANTITY = 3L;
    private static final Long ANOTHER_QUANTITY = 7L;
    private static final InventoryStatusEnum INVENTORY_STATUS_ENUM = InventoryStatusEnum.INSTOCK;
    private static final InventoryStatusEnum ANOTHER_INVENTORY_STATUS_ENUM = InventoryStatusEnum.LOWSTOCK;
    private static final CategoryEnum CATEGORY_ENUM = CategoryEnum.Accessories;
    private static final CategoryEnum ANOTHER_CATEGORY_ENUM = CategoryEnum.Clothing;
    private static final String IMAGE = "image";
    private static final String ANOTHER_IMAGE = "image2";
    private static final Long RATING = 4L;
    private static final Long ANOTHER_RATING = 8L;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Mock
    private Product productMock;

    @MockBean
    private ProductRepository productRepositoryMock;


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(productRepositoryMock, productMock);
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
        when(productRepositoryMock.findAll()).thenReturn(Collections.emptyList());

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
        when(productRepositoryMock.findAll()).thenReturn(List.of(new Product(
                ID, CODE,
                NAME,
                DESCRIPTION,
                PRICE,
                QUANTITY,
                INVENTORY_STATUS_ENUM,
                CATEGORY_ENUM, IMAGE, RATING)));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
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
        when(productRepositoryMock.findAll()).thenReturn(Stream.generate(() -> new Product(
                ID, CODE,
                NAME,
                DESCRIPTION,
                PRICE,
                QUANTITY,
                INVENTORY_STATUS_ENUM,
                CATEGORY_ENUM, IMAGE, RATING)).limit(nbElements).collect(Collectors.toList()));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
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

    @Test
    public void givenAJsonBody_whenCreatingProduct_thenCallRepositorySaveAndReturnCreatedId() throws Exception {
        when(productMock.getId()).thenReturn(ID);
        when(productRepositoryMock.save(productArgumentCaptor.capture())).thenReturn(productMock);
        String jsonInput= """
                {
                    "code": "%s",
                    "name": "%s",
                    "description": "%s",
                    "price": %d,
                    "quantity": %d,
                    "inventoryStatus": "%s",
                    "category": "%s",
                    "image": "%s",
                    "rating": %d
                }
                """.formatted(CODE, NAME, DESCRIPTION, PRICE, QUANTITY, INVENTORY_STATUS_ENUM, CATEGORY_ENUM, IMAGE, RATING);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/products")
                        .content(jsonInput)
                        .accept(MediaType.ALL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Product> allCapturedProducts = productArgumentCaptor.getAllValues();
        assertEquals(1, allCapturedProducts.size());
        Product capturedProduct = allCapturedProducts.get(0);
        assertNull(capturedProduct.getId());
        assertEquals(CODE, capturedProduct.getCode());
        assertEquals(NAME, capturedProduct.getName());
        assertEquals(DESCRIPTION, capturedProduct.getDescription());
        assertEquals(PRICE, capturedProduct.getPrice());
        assertEquals(QUANTITY, capturedProduct.getQuantity());
        assertEquals(INVENTORY_STATUS_ENUM, capturedProduct.getInventoryStatus());
        assertEquals(CATEGORY_ENUM, capturedProduct.getCategory());
        assertEquals(IMAGE, capturedProduct.getImage());
        assertEquals(RATING, capturedProduct.getRating());

        assertEquals("%s".formatted(ID), mvcResult.getResponse().getContentAsString());

    }

    @Test
    public void givenAnId_whenDeletingProduct_thenCallRepositoryDeleteById() throws Exception {
        doNothing().when(productRepositoryMock).deleteById(longArgumentCaptor.capture());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.delete("/products/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
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

        List<Long> allLongs = longArgumentCaptor.getAllValues();
        assertEquals(1, allLongs.size());
        assertEquals(ID, allLongs.get(0));

    }

    @Test
    public void givenABadId_whenGettingProduct_thenReturnNotFound() throws Exception {
        when(productRepositoryMock.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        List<Long> allLongs = longArgumentCaptor.getAllValues();
        assertEquals(1, allLongs.size());
        assertEquals(ID, allLongs.get(0));

    }

    @Test
    public void givenAnExistingId_whenGettingProduct_thenReturnProductDetails() throws Exception {
        when(productRepositoryMock.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(new Product(
                ANOTHER_ID, CODE,
                NAME,
                DESCRIPTION,
                PRICE,
                QUANTITY,
                INVENTORY_STATUS_ENUM,
                CATEGORY_ENUM, IMAGE, RATING)));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/products/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(ANOTHER_ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(PRICE.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", equalTo(QUANTITY.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventoryStatus", equalTo(INVENTORY_STATUS_ENUM.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", equalTo(CATEGORY_ENUM.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", equalTo(IMAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", equalTo(RATING.intValue())))
                .andReturn();

        List<Long> allLongs = longArgumentCaptor.getAllValues();
        assertEquals(1, allLongs.size());
        assertEquals(ID, allLongs.get(0));

    }

    @Test
    public void givenABadId_whenPatchingProduct_thenDoNothingAndReturnOkWithNoData() throws Exception {
        when(productRepositoryMock.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());
        verify(productRepositoryMock, never()).save(any());
        String jsonInput= """
                {
                    "code": "%s",
                    "name": "%s",
                    "description": "%s",
                    "price": %d,
                    "quantity": %d,
                    "inventoryStatus": "%s",
                    "category": "%s",
                    "image": "%s",
                    "rating": %d
                }
                """.formatted(CODE, NAME, DESCRIPTION, PRICE, QUANTITY, INVENTORY_STATUS_ENUM, CATEGORY_ENUM, IMAGE, RATING);


        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.patch("/products/{id}", ID)
                        .content(jsonInput)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Long> allLongs = longArgumentCaptor.getAllValues();
        assertEquals(1, allLongs.size());
        assertEquals(ID, allLongs.get(0));

        assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void givenAnExistingId_whenUpdatingItem_thenSaveUpdatedProductAndReturnUpdatedProductDetails() throws Exception {
        when(productRepositoryMock.findById(longArgumentCaptor.capture())).thenReturn(Optional.of(new Product(
                ID, ANOTHER_CODE,
                ANOTHER_NAME,
                ANOTHER_DESCRIPTION,
                ANOTHER_PRICE,
                ANOTHER_QUANTITY,
                ANOTHER_INVENTORY_STATUS_ENUM,
                ANOTHER_CATEGORY_ENUM, ANOTHER_IMAGE, ANOTHER_RATING)));
        when(productRepositoryMock.save(productArgumentCaptor.capture())).thenAnswer(i -> i.getArguments()[0]);
        String jsonInput= """
                {
                    "code": "%s",
                    "name": "%s",
                    "description": "%s",
                    "price": %d,
                    "quantity": %d,
                    "inventoryStatus": "%s",
                    "category": "%s",
                    "image": "%s",
                    "rating": %d
                }
                """.formatted(CODE, NAME, DESCRIPTION, PRICE, QUANTITY, INVENTORY_STATUS_ENUM, CATEGORY_ENUM, IMAGE, RATING);


        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.patch("/products/{id}", ID)
                        .content(jsonInput)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(CODE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", equalTo(PRICE.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", equalTo(QUANTITY.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inventoryStatus", equalTo(INVENTORY_STATUS_ENUM.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category", equalTo(CATEGORY_ENUM.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", equalTo(IMAGE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating", equalTo(RATING.intValue())))
                .andReturn();

        List<Long> allLongs = longArgumentCaptor.getAllValues();
        assertEquals(1, allLongs.size());
        assertEquals(ID, allLongs.get(0));

        List<Product> allCapturedProducts = productArgumentCaptor.getAllValues();
        assertEquals(1, allCapturedProducts.size());
        Product capturedProduct = allCapturedProducts.get(0);
        assertEquals(ID, capturedProduct.getId());
        assertEquals(CODE, capturedProduct.getCode());
        assertEquals(NAME, capturedProduct.getName());
        assertEquals(DESCRIPTION, capturedProduct.getDescription());
        assertEquals(PRICE, capturedProduct.getPrice());
        assertEquals(QUANTITY, capturedProduct.getQuantity());
        assertEquals(INVENTORY_STATUS_ENUM, capturedProduct.getInventoryStatus());
        assertEquals(CATEGORY_ENUM, capturedProduct.getCategory());
        assertEquals(IMAGE, capturedProduct.getImage());
        assertEquals(RATING, capturedProduct.getRating());
    }
}