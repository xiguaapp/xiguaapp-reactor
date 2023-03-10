package com.cn.xiguaapp.r2dbc.common.api.crud.entity;

import com.cn.xiguaapp.r2dbc.orm.param.Term;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * @author xiguaapp
 * @desc
 * @since 1.0 22:25
 */
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Operation
public @interface QueryNoPagingOperation {

    /**
     * The HTTP method for this operation.
     *
     * @return the HTTP method of this operation
     **/
    @AliasFor(annotation = Operation.class)
    String method() default "";

    /**
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     *
     * @return the list of tags associated with this operation
     **/
    @AliasFor(annotation = Operation.class)
    String[] tags() default {};

    /**
     * Provides a brief description of this operation. Should be 120 characters or less for proper visibility in Swagger-UI.
     *
     * @return a summary of this operation
     **/
    @AliasFor(annotation = Operation.class)
    String summary() default "";

    /**
     * A verbose description of the operation.
     *
     * @return a description of this operation
     **/
    @AliasFor(annotation = Operation.class)
    String description() default "";

    /**
     * Request body associated to the operation.
     *
     * @return a request body.
     */
    @AliasFor(annotation = Operation.class)
    RequestBody requestBody() default @RequestBody();

    /**
     * Additional external documentation for this operation.
     *
     * @return additional documentation about this operation
     **/
    @AliasFor(annotation = Operation.class)
    ExternalDocumentation externalDocs() default @ExternalDocumentation();

    /**
     * The operationId is used by third-party tools to uniquely identify this operation.
     *
     * @return the ID of this operation
     **/
    @AliasFor(annotation = Operation.class)
    String operationId() default "";

    /**
     * An optional array of parameters which will be added to any automatically detected parameters in the method itself.
     *
     * @return the list of parameters for this operation
     **/
    @AliasFor(annotation = Operation.class)
    Parameter[] parameters() default {
            @Parameter(name = "where", description = "???????????????,???terms????????????", example = "id = 1", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "orderBy", description = "???????????????,???sorts????????????", example = "id desc", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "includes", description = "?????????????????????,????????????????????????", example = "id", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "excludes", description = "?????????????????????,????????????????????????",  schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "terms[0].column", description = "??????????????????", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "terms[0].termType", description = "????????????", schema = @Schema(implementation = String.class), example = "like", in = ParameterIn.QUERY),
            @Parameter(name = "terms[0].type", description = "????????????????????????", schema = @Schema(implementation = Term.Type.class), in = ParameterIn.QUERY),
            @Parameter(name = "terms[0].value", description = "?????????", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "sorts[0].name", description = "????????????", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
            @Parameter(name = "sorts[0].order", description = "??????,asc??????desc", schema = @Schema(implementation = String.class), in = ParameterIn.QUERY),
    };

    /**
     * The list of possible responses as they are returned from executing this operation.
     *
     * @return the list of responses for this operation
     **/
    @AliasFor(annotation = Operation.class)
    ApiResponse[] responses() default {};

    /**
     * Allows an operation to be marked as deprecated.  Alternatively use the @Deprecated annotation
     *
     * @return whether or not this operation is deprecated
     **/
    @AliasFor(annotation = Operation.class)
    boolean deprecated() default false;

    /**
     * A declaration of which security mechanisms can be used for this operation.
     *
     * @return the array of security requirements for this Operation
     */
    @AliasFor(annotation = Operation.class)
    SecurityRequirement[] security() default {};

    /**
     * An alternative server array to service this operation.
     *
     * @return the list of servers hosting this operation
     **/
    @AliasFor(annotation = Operation.class)
    Server[] servers() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    @AliasFor(annotation = Operation.class)
    Extension[] extensions() default {};

    /**
     * Allows this operation to be marked as hidden
     *
     * @return whether or not this operation is hidden
     */
    @AliasFor(annotation = Operation.class)
    boolean hidden() default false;

    /**
     * Ignores JsonView annotations while resolving operations and types.
     *
     * @return whether or not to ignore JsonView annotations
     */
    @AliasFor(annotation = Operation.class)
    boolean ignoreJsonView() default false;
}
