package io.smallrye.openapi.runtime.io.parameter;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.openapi.models.parameters.Parameter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.smallrye.openapi.runtime.io.JsonUtil;
import io.smallrye.openapi.runtime.io.ObjectWriter;
import io.smallrye.openapi.runtime.io.content.ContentWriter;
import io.smallrye.openapi.runtime.io.example.ExampleWriter;
import io.smallrye.openapi.runtime.io.extension.ExtensionWriter;
import io.smallrye.openapi.runtime.io.schema.SchemaWriter;

/**
 * Writing Parameter to json
 * 
 * @see https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#parameter-object
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class ParameterWriter {

    private ParameterWriter() {
    }

    /**
     * Writes a map of {@link Parameter} to the JSON tree.
     * 
     * @param parent
     * @param parameters
     */
    public static void writeParameters(ObjectNode parent, Map<String, Parameter> parameters) {
        if (parameters == null) {
            return;
        }
        ObjectNode parametersNode = parent.putObject(ParameterConstant.PROP_PARAMETERS);
        for (String parameterName : parameters.keySet()) {
            writeParameter(parametersNode, parameters.get(parameterName), parameterName);
        }
    }

    /**
     * Writes a {@link Parameter} object to the JSON tree.
     * 
     * @param parent
     * @param model
     * @param name
     */
    private static void writeParameter(ObjectNode parent, Parameter model, String name) {
        if (model == null) {
            return;
        }
        ObjectNode node = parent.putObject(name);
        writeParameter(node, model);
    }

    /**
     * Writes a list of {@link Parameter} to the JSON tree.
     * 
     * @param parent
     * @param models
     */
    public static void writeParameterList(ObjectNode parent, List<Parameter> models) {
        if (models == null) {
            return;
        }
        ArrayNode node = parent.putArray(ParameterConstant.PROP_PARAMETERS);
        for (Parameter model : models) {
            ObjectNode paramNode = node.addObject();
            writeParameter(paramNode, model);
        }
    }

    /**
     * Writes a {@link Parameter} into the JSON node.
     * 
     * @param node
     * @param model
     */
    private static void writeParameter(ObjectNode node, Parameter model) {
        JsonUtil.stringProperty(node, ParameterConstant.PROP_$REF, model.getRef());
        JsonUtil.stringProperty(node, ParameterConstant.PROP_NAME, model.getName());
        JsonUtil.enumProperty(node, ParameterConstant.PROP_IN, model.getIn());
        JsonUtil.stringProperty(node, ParameterConstant.PROP_DESCRIPTION, model.getDescription());
        JsonUtil.booleanProperty(node, ParameterConstant.PROP_REQUIRED, model.getRequired());
        SchemaWriter.writeSchema(node, model.getSchema(), ParameterConstant.PROP_SCHEMA);
        JsonUtil.booleanProperty(node, ParameterConstant.PROP_ALLOW_EMPTY_VALUE, model.getAllowEmptyValue());
        JsonUtil.booleanProperty(node, ParameterConstant.PROP_DEPRECATED, model.getDeprecated());
        JsonUtil.enumProperty(node, ParameterConstant.PROP_STYLE, model.getStyle());
        JsonUtil.booleanProperty(node, ParameterConstant.PROP_EXPLODE, model.getExplode());
        JsonUtil.booleanProperty(node, ParameterConstant.PROP_ALLOW_RESERVED, model.getAllowReserved());
        ObjectWriter.writeObject(node, ParameterConstant.PROP_EXAMPLE, model.getExample());
        ExampleWriter.writeExamples(node, model.getExamples());
        ContentWriter.writeContent(node, model.getContent());
        ExtensionWriter.writeExtensions(node, model);
    }

}
