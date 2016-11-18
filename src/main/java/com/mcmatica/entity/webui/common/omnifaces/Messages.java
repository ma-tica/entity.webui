/*
 * Copyright 2016 OmniFaces
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.mcmatica.entity.webui.common.omnifaces;

import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.convert.ConverterException;
import javax.faces.validator.ValidatorException;

/**
 * <p>
 * Collection of utility methods for the JSF API with respect to working with {@link FacesMessage}.
 *
 * <h3>Usage</h3>
 * <p>
 * Some examples:
 * <pre>
 * // In a validator.
 * throw new ValidatorException(Messages.createError("Invalid input."));
 * </pre>
 * <pre>
 * // In a validator, as extra message on another component.
 * Messages.addError("someFormId:someInputId", "This is also invalid.");
 * </pre>
 * <pre>
 * // In a managed bean action method.
 * Messages.addGlobalError("Unknown login, please try again.");
 * </pre>
 * <pre>
 * // In a managed bean action method which uses Post-Redirect-Get.
 * Messages.addFlashGlobalInfo("New item with id {0} is successfully added.", item.getId());
 * return "items?faces-redirect=true";
 * </pre>
 * <p>
 * There is also a builder which also allows you to set the message detail. Some examples:
 * <pre>
 * // In a validator.
 * throw new ValidatorException(Messages.create("Invalid input.").detail("Value {0} is not expected.", value).get());
 * </pre>
 * <pre>
 * // In a validator, as extra message on another component.
 * Messages.create("This is also invalid.").error().add("someFormId:someInputId");
 * </pre>
 * <pre>
 * // In a managed bean action method.
 * Messages.create("Unknown login, please try again.").error().add();
 * </pre>
 * <pre>
 * // In a managed bean action method which uses Post-Redirect-Get.
 * Messages.create("New item with id {0} is successfully added.", item.getId()).flash().add();
 * return "items?faces-redirect=true";
 * </pre>
 *
 * <h3>Message resolver</h3>
 * <p>
 * It also offers the possibility to set a custom message resolver so that you can control the way how messages are been
 * resolved. You can for example supply an implementation wherein the message is been treated as for example a resource
 * bundle key. Here's an example:
 * <pre>
 * Messages.setResolver(new Messages.Resolver() {
 *     private static final String BASE_NAME = "com.example.i18n.messages";
 *     public String getMessage(String message, Object... params) {
 *         ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, Faces.getLocale());
 *         if (bundle.containsKey(message)) {
 *             message = bundle.getString(message);
 *         }
 *         return params.length &gt; 0 ? MessageFormat.format(message, params) : message;
 *     }
 * });
 * </pre>
 * <p>
 * There is already a default resolver which just delegates the message and the parameters straight to
 * {@link MessageFormat#format(String, Object...)}. Note that the resolver can be set only once. It's recommend to do
 * it early during webapp's startup, for example with a {@link ServletContextListener} as {@link WebListener}, or a
 * {@link ServletContainerInitializer} in custom JAR, or a {@link ApplicationScoped} bean, or an eagerly initialized
 * {@link Startup} bean.
 *
 * <h3>Design notice</h3>
 * <p>
 * Note that all of those shortcut methods by design only sets the message summary and ignores the message detail (it
 * is not possible to offer varargs to parameterize <em>both</em> the summary and the detail). The message summary is
 * exactly the information which is by default displayed in the <code>&lt;h:message(s)&gt;</code>, while the detail is
 * by default only displayed when you explicitly set the <code>showDetail="true"</code> attribute.
 * <p>
 * To create a {@link FacesMessage} with a message detail as well, use the {@link Message} builder as you can obtain by
 * {@link Messages#create(String, Object...)}.
 *
 * @author Bauke Scholtz
 */
public final class Messages {

	// Private constants ----------------------------------------------------------------------------------------------

	private static final String ERROR_RESOLVER_ALREADY_SET = "The resolver can be set only once.";

	// Message resolver -----------------------------------------------------------------------------------------------

	/**
	 * The message resolver allows the developers to change the way how messages are resolved.
	 *
	 * @author Bauke Scholtz
	 */
	public interface Resolver {

		/**
		 * Returns the resolved message based on the given message and parameters.
		 * @param message The message which can be treated as for example a resource bundle key.
		 * @param params The message format parameters, if any.
		 * @return The resolved message.
		 */
		String getMessage(String message, Object... params);

	}

	/**
	 * This is the default message resolver.
	 */
	private static final Resolver DEFAULT_RESOLVER = new Resolver() {

		@Override
		public String getMessage(String message, Object... params) {
			return (params == null) ? message : MessageFormat.format(message, params);
		}

	};

	/**
	 * Initialize with the default resolver.
	 */
	private static Resolver resolver = DEFAULT_RESOLVER;

	/**
	 * Set the custom message resolver. It can be set only once. It's recommend to do it early during webapp's startup,
	 * for example with a {@link ServletContextListener} as {@link WebListener}, or a
	 * {@link ServletContainerInitializer} in custom JAR, or a {@link ApplicationScoped} bean, or an eagerly initialized
	 * {@link Startup} bean.
	 * @param resolver The custom message resolver.
	 * @throws IllegalStateException When the resolver has already been set.
	 */
	public static void setResolver(Resolver resolver) {
		if (Messages.resolver == DEFAULT_RESOLVER) {
			Messages.resolver = resolver;
		}
		else {
			throw new IllegalStateException(ERROR_RESOLVER_ALREADY_SET);
		}
	}

	// Constructors ---------------------------------------------------------------------------------------------------

	private Messages() {
		// Hide constructor.
	}

	// Builder --------------------------------------------------------------------------------------------------------

	/**
	 * Create a faces message with the default INFO severity and the given message body which is formatted with the
	 * given parameters as summary message. To set the detail message, use {@link Message#detail(String, Object...)}.
	 * To change the default INFO severity, use {@link Message#warn()}, {@link Message#error()}, or
	 * {@link Message#fatal()}. To make it a flash message, use {@link Message#flash()}. To finally add it to the faces
	 * context, use either {@link Message#add(String)} to add it for a specific client ID, or {@link Message#add()} to
	 * add it as a global message.
	 * @param message The message body.
	 * @param params The message format parameters, if any.
	 * @return The {@link Message} builder.
	 * @see Messages#createInfo(String, Object...)
	 * @see Resolver#getMessage(String, Object...)
	 * @since 1.1
	 */
	public static Message create(String message, Object... params) {
		return new Message(createInfo(message, params));
	}

	/**
	 * Faces message builder.
	 *
	 * @author Bauke Scholtz
	 * @since 1.1
	 */
	public static final class Message {

		private FacesMessage message;

		private Message(FacesMessage message) {
			this.message = message;
		}

		/**
		 * Set the detail message of the current message.
		 * @param detail The detail message to be set on the current message.
		 * @param params The detail message format parameters, if any.
		 * @return The current {@link Message} instance for further building.
		 * @see FacesMessage#setDetail(String)
		 */
		public Message detail(String detail, Object... params) {
			message.setDetail(resolver.getMessage(detail, params));
			return this;
		}

		/**
		 * Set the severity of the current message to WARN. Note: it defaults to INFO already.
		 * @return The current {@link Message} instance for further building.
		 * @see FacesMessage#setSeverity(javax.faces.application.FacesMessage.Severity)
		 */
		public Message warn() {
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			return this;
		}

		/**
		 * Set the severity of the current message to ERROR. Note: it defaults to INFO already.
		 * @return The current {@link Message} instance for further building.
		 * @see FacesMessage#setSeverity(javax.faces.application.FacesMessage.Severity)
		 */
		public Message error() {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			return this;
		}

		/**
		 * Set the severity of the current message to FATAL. Note: it defaults to INFO already.
		 * @return The current {@link Message} instance for further building.
		 * @see FacesMessage#setSeverity(javax.faces.application.FacesMessage.Severity)
		 */
		public Message fatal() {
			message.setSeverity(FacesMessage.SEVERITY_FATAL);
			return this;
		}

		/**
		 * Returns the so far built message.
		 * @return The so far built message.
		 */
		public FacesMessage get() {
			return message;
		}

	}

	// Shortcuts - create message -------------------------------------------------------------------------------------

	/**
	 * Create a faces message of the given severity with the given message body which is formatted with the given
	 * parameters. Useful when a faces message is needed to construct a {@link ConverterException} or a
	 * {@link ValidatorException}.
	 * @param severity The severity of the faces message.
	 * @param message The message body.
	 * @param params The message format parameters, if any.
	 * @return A new faces message of the given severity with the given message body which is formatted with the given
	 * parameters.
	 * @see Resolver#getMessage(String, Object...)
	 */
	public static FacesMessage create(FacesMessage.Severity severity, String message, Object... params) {
		return new FacesMessage(severity, resolver.getMessage(message, params), null);
	}

	/**
	 * Create an INFO faces message with the given message body which is formatted with the given parameters.
	 * @param message The message body.
	 * @param params The message format parameters, if any.
	 * @return A new INFO faces message with the given message body which is formatted with the given parameters.
	 * @see #create(javax.faces.application.FacesMessage.Severity, String, Object...)
	 */
	public static FacesMessage createInfo(String message, Object... params) {
		return create(FacesMessage.SEVERITY_INFO, message, params);
	}

	/**
	 * Create a WARN faces message with the given message body which is formatted with the given parameters.
	 * @param message The message body.
	 * @param params The message format parameters, if any.
	 * @return A new WARN faces message with the given message body which is formatted with the given parameters.
	 * @see #create(javax.faces.application.FacesMessage.Severity, String, Object...)
	 */
	public static FacesMessage createWarn(String message, Object... params) {
		return create(FacesMessage.SEVERITY_WARN, message, params);
	}

	/**
	 * Create an ERROR faces message with the given message body which is formatted with the given parameters.
	 * @param message The message body.
	 * @param params The message format parameters, if any.
	 * @return A new ERROR faces message with the given message body which is formatted with the given parameters.
	 * @see #create(javax.faces.application.FacesMessage.Severity, String, Object...)
	 */
	public static FacesMessage createError(String message, Object... params) {
		return create(FacesMessage.SEVERITY_ERROR, message, params);
	}

	/**
	 * Create a FATAL faces message with the given message body which is formatted with the given parameters.
	 * @param message The message body.
	 * @param params The message format parameters, if any.
	 * @return A new FATAL faces message with the given message body which is formatted with the given parameters.
	 * @see #create(javax.faces.application.FacesMessage.Severity, String, Object...)
	 */
	public static FacesMessage createFatal(String message, Object... params) {
		return create(FacesMessage.SEVERITY_FATAL, message, params);
	}

	// Shortcuts - add message ----------------------------------------------------------------------------------------


}