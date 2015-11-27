package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.webapp.panels.attribute.AttributeInputPanel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.GrammarsDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class KindPanel extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(KindPanel.class);
    private KindModel kind;

    public KindPanel(String markupId, ResourceTypeDocument document) {
        super(markupId);

        setKind(document);
        this.add(new KindForm("kindForm"));
    }

    public KindPanel setKind(ResourceTypeDocument document) {
        this.kind = null == document ? new KindModel("", "") : parseKind(parseClassification(document).getKindType());
        return this;
    }

    private ClassificationDocument.Classification parseClassification(ResourceTypeDocument document) {
        GrammarsDocument.Grammars grammars = document.getResourceType().getGrammars();
        XmlObject[] classifications = grammars.selectChildren("urn:xmpp:occi-classification", "Classification");
        return (ClassificationDocument.Classification) classifications[0];
    }

    private KindModel parseKind(CategoryClassification classification) {
        KindModel kind = new KindModel(classification.getTerm(), classification.getSchema());
        for (AttributeClassificationDocument.AttributeClassification a : classification.getAttributeClassificationArray()) {
            AttributeModel attribute = new AttributeModel(a.getName(), a.getType().toString(), a.getRequired(), a.getMutable(), a.getDescription());
            kind.addAttribute(attribute);
        }
        return kind;
    }

    private class KindForm extends Form {

        public KindForm(String markupId) {
            super(markupId);

            add(new AttributeInputPanel("attributePanel", new LoadableDetachableModel<List<AttributeModel>>() {
                @Override
                protected List<AttributeModel> load() {
                    return new ArrayList<>(kind.getAttributes());
                }
            }));
            add(new AjaxSubmitLink("kindSubmit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    for (AttributeModel a : kind.getAttributes()) {
                        logger.info(a.toString());
                    }
                }
            });
        }
    }
}
