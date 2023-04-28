/*
 * Copyright 2015-2023 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.io;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

import org.apiguardian.api.API;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * {@code TempDirFactory} defines the SPI for creating temporary directories
 * programmatically.
 *
 * <p>A temporary directory factory is typically used to gain more control on
 * the temporary directory creation, like defining the parent directory or even
 * the file system that should be used.
 *
 * <p>Concrete implementations must have a <em>default constructor</em>.
 *
 * <p>A {@link TempDirFactory} can be configured <em>locally</em>
 * for a test class field or method parameter via the {@link TempDir @TempDir}
 * annotation.
 *
 * @since 5.10
 * @see TempDir @TempDir
 */
@FunctionalInterface
@API(status = EXPERIMENTAL, since = "5.10")
public interface TempDirFactory extends Closeable {

	/**
	 * Create a new temporary directory, using the given prefix to generate its name.
	 * Depending on the implementation, the resulting {@code Path} may or may not be
	 * associated with the default {@code FileSystem}.
	 *
	 * @param context the current extension context; never {@code null}
	 * @return the path to the newly created directory that did not exist before this method was invoked; never {@code null}
	 * @throws Exception in case of failures
	 */
	Path createTempDirectory(ExtensionContext context) throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	default void close() throws IOException {
	}

	/**
	 * Standard temporary directory factory that delegates to
	 * {@link Files#createTempDirectory}.
	 *
	 * @see Files#createTempDirectory(String, FileAttribute[])
	 */
	class Standard implements TempDirFactory {

		public static final TempDirFactory INSTANCE = new Standard();

		private static final String TEMP_DIR_PREFIX = "junit";

		public Standard() {
		}

		@Override
		public Path createTempDirectory(ExtensionContext context) throws IOException {
			return Files.createTempDirectory(TEMP_DIR_PREFIX);
		}

	}

}
