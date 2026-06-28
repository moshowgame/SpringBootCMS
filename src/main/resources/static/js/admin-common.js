/**
 * SpringBootCMS Admin Common JS
 * jQuery3 + Bootstrap5
 */

// CSRF Token 获取（从Cookie读取）
const CmsCsrf = {
    getToken: function() {
        const name = 'XSRF-TOKEN';
        const cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
            let cookie = cookies[i].trim();
            if (cookie.startsWith(name + '=')) {
                return decodeURIComponent(cookie.substring(name.length + 1));
            }
        }
        return null;
    }
};

// 全局 jQuery AJAX 拦截器：为所有 POST/PUT/DELETE/PATCH 请求自动携带 CSRF Token
if (typeof $ !== 'undefined' && $.ajaxSetup) {
    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            const method = (settings.type || 'GET').toUpperCase();
            if (['POST', 'PUT', 'DELETE', 'PATCH'].includes(method)) {
                const token = CmsCsrf.getToken();
                if (token) {
                    xhr.setRequestHeader('X-XSRF-TOKEN', token);
                }
            }
        }
    });
}

// 通用API请求封装（自动携带CSRF Token）
const CmsApi = {
    contextPath: (typeof window.CMS_CONTEXT_PATH === 'string') ? window.CMS_CONTEXT_PATH : '',

    _ajax: function(url, options) {
        const csrfToken = CmsCsrf.getToken();
        const ajaxOptions = {
            url: this.contextPath + url,
            dataType: 'json',
            headers: csrfToken ? { 'X-XSRF-TOKEN': csrfToken } : {},
            ...options
        };
        // POST/PUT/DELETE 需要 CSRF
        if (['POST', 'PUT', 'DELETE', 'PATCH'].includes((ajaxOptions.type || 'GET').toUpperCase()) && csrfToken) {
            ajaxOptions.headers['X-XSRF-TOKEN'] = csrfToken;
        }
        return $.ajax(ajaxOptions);
    },

    post: function(url, data, callback) {
        return this._ajax(url, {
            type: 'POST',
            data: data,
            success: function(res) {
                if (typeof callback === 'function') callback(res);
            },
            error: function(xhr) {
                CmsUtil.showToast('请求失败: ' + (xhr.responseText || '未知错误'), 'danger');
            }
        });
    },

    postJson: function(url, data, callback) {
        return this._ajax(url, {
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(res) {
                if (typeof callback === 'function') callback(res);
            },
            error: function(xhr) {
                CmsUtil.showToast('请求失败: ' + (xhr.responseText || '未知错误'), 'danger');
            }
        });
    },

    get: function(url, callback) {
        return this._ajax(url, {
            type: 'GET',
            success: function(res) {
                if (typeof callback === 'function') callback(res);
            },
            error: function(xhr) {
                CmsUtil.showToast('请求失败: ' + (xhr.responseText || '未知错误'), 'danger');
            }
        });
    }
};

// 通用工具
const CmsUtil = {
    // 显示Toast提示
    showToast: function(message, type) {
        type = type || 'success';
        const iconMap = {
            success: 'bi-check-circle-fill',
            danger: 'bi-exclamation-triangle-fill',
            warning: 'bi-exclamation-circle-fill',
            info: 'bi-info-circle-fill'
        };
        const bgMap = {
            success: 'bg-success',
            danger: 'bg-danger',
            warning: 'bg-warning',
            info: 'bg-info'
        };

        let container = $('.cms-toast');
        if (container.length === 0) {
            container = $('<div class="cms-toast"></div>');
            $('body').append(container);
        }

        const toast = $(`
            <div class="toast align-items-center text-white ${bgMap[type]} border-0 show" role="alert">
                <div class="d-flex">
                    <div class="toast-body">
                        <i class="bi ${iconMap[type]} me-2"></i>${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            </div>
        `);

        container.append(toast);
        setTimeout(function() {
            toast.remove();
        }, 3000);
    },

    // 确认对话框
    confirm: function(message, onConfirm) {
        if (confirm(message)) {
            if (typeof onConfirm === 'function') onConfirm();
        }
    },

    /**
     * 显示攻击检测警告横幅
     * 用于服务端 AttackDetectionFilter / 业务层检测到的攻击拦截
     * @param {string} message 服务端返回的拦截消息
     */
    showAttackWarning: function(message) {
        // 提取攻击类型关键字（在括号前）
        let safeMessage = CmsUtil.escapeHtml(message || '检测到可疑输入');
        let container = $('.cms-attack-warning');
        if (container.length === 0) {
            container = $('<div class="cms-attack-warning" style="position:fixed;top:80px;right:20px;z-index:9999;max-width:480px;"></div>');
            $('body').append(container);
        }
        const alert = $(`
            <div class="alert alert-danger alert-dismissible fade show shadow" role="alert">
                <div class="d-flex align-items-start">
                    <i class="bi bi-shield-exclamation me-2" style="font-size:1.5rem;"></i>
                    <div class="flex-grow-1">
                        <strong>输入已被拦截</strong>
                        <p class="mb-0 small">${safeMessage}</p>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        `);
        container.empty().append(alert);
        // 8 秒后自动消失
        setTimeout(function() {
            alert.fadeOut(500, function() { $(this).remove(); });
        }, 8000);
    },

    // HTML 转义工具：用于在 JS 中安全拼接用户可控字符串
    escapeHtml: function(s) {
        if (s == null) return '';
        if (typeof s !== 'string') s = String(s);
        return s.replace(/[&<>"']/g, function(c) {
            return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c];
        });
    },

    // HTML 属性转义（同 escapeHtml，但语义化表示用于 HTML 属性）
    escapeAttr: function(s) {
        return CmsUtil.escapeHtml(s);
    },

    // 格式化日期
    formatDate: function(dateStr) {
        if (!dateStr) return '-';
        return dateStr;
    },

    // 状态标签
    statusBadge: function(status) {
        if (status === 1) {
            return '<span class="badge bg-success badge-status">启用</span>';
        } else {
            return '<span class="badge bg-secondary badge-status">停用</span>';
        }
    },

    // 分页HTML生成
    renderPagination: function(total, page, limit, onPageChange) {
        const totalPages = Math.ceil(total / limit);
        if (totalPages <= 1) return '';

        let html = '<nav><ul class="pagination pagination-sm mb-0">';
        html += `<li class="page-item ${page <= 1 ? 'disabled' : ''}">
                    <a class="page-link" href="#" data-page="${page - 1}">&laquo;</a>
                 </li>`;

        const startPage = Math.max(1, page - 2);
        const endPage = Math.min(totalPages, page + 2);

        for (let i = startPage; i <= endPage; i++) {
            html += `<li class="page-item ${i === page ? 'active' : ''}">
                        <a class="page-link" href="#" data-page="${i}">${i}</a>
                     </li>`;
        }

        html += `<li class="page-item ${page >= totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="#" data-page="${page + 1}">&raquo;</a>
                 </li>`;
        html += '</ul></nav>';

        return html;
    }
};

// 通用CRUD表格类
class CmsTable {
    constructor(options) {
        this.apiList = options.apiList;
        this.apiDelete = options.apiDelete;
        this.columns = options.columns;
        this.searchFields = options.searchFields || [];
        this.tableBody = $(options.tableBody || '#dataTableBody');
        this.paginationEl = $(options.paginationEl || '#pagination');
        this.page = 1;
        this.limit = options.limit || 10;
        this.searchParams = {};
    }

    load() {
        const self = this;
        const params = { page: this.page, limit: this.limit };
        if (this.searchParams.searchParams) {
            params.searchParams = this.searchParams.searchParams;
        }

        CmsApi.post(this.apiList, params, function(res) {
            if (res.code === 200) {
                self.render(res.data || []);
                self.renderPagination(res.count || 0);
            } else {
                // 服务端攻击检测拦截 → 显示警告横幅
                if (res.msg && res.msg.indexOf('攻击已拦截') !== -1) {
                    CmsUtil.showAttackWarning(res.msg);
                } else {
                    CmsUtil.showToast(res.msg || '加载失败', 'danger');
                }
            }
        });
    }

    render(data) {
        const self = this;
        let html = '';
        if (data.length === 0) {
            html = `<tr><td colspan="${this.columns.length + 1}" class="text-center text-muted py-4">暂无数据</td></tr>`;
        } else {
            data.forEach(function(row, idx) {
                html += '<tr>';
                html += `<td>${(self.page - 1) * self.limit + idx + 1}</td>`;
                self.columns.forEach(function(col) {
                    let val = row[col.field];
                    if (col.render) {
                        // 开发者自定义渲染（已明确控制输出，信任返回的 HTML 片段）
                        val = col.render(val, row);
                    } else {
                        // 默认渲染：必须对原始值进行 HTML 转义，防止存储型 XSS
                        val = CmsUtil.escapeHtml(val);
                    }
                    html += `<td>${val !== null && val !== undefined ? val : '-'}</td>`;
                });
                html += '</tr>';
            });
        }
        this.tableBody.html(html);
    }

    renderPagination(total) {
        const self = this;
        const totalPages = Math.ceil(total / this.limit);
        let html = `<div class="cms-pagination">
            <span>共 ${total} 条记录，第 ${this.page}/${totalPages || 1} 页</span>`;
        html += CmsUtil.renderPagination(total, this.page, this.limit);
        html += '</div>';
        this.paginationEl.html(html);

        this.paginationEl.find('.page-link').off('click').on('click', function(e) {
            e.preventDefault();
            const p = parseInt($(this).data('page'));
            if (p >= 1 && p <= totalPages) {
                self.page = p;
                self.load();
            }
        });
    }

    search(params) {
        this.searchParams = { searchParams: JSON.stringify(params) };
        this.page = 1;
        this.load();
    }

    delete(id, name) {
        const self = this;
        CmsUtil.confirm('确定要删除' + (name ? ' "' + name + '"' : '') + '吗？', function() {
            CmsApi.post(self.apiDelete, { id: id }, function(res) {
                if (res.code === 200) {
                    CmsUtil.showToast('删除成功', 'success');
                    self.load();
                } else {
                    CmsUtil.showToast(res.msg || '删除失败', 'danger');
                }
            });
        });
    }
}

// 侧边栏折叠
$(document).ready(function() {
    $('.toggle-sidebar').on('click', function() {
        $('.cms-sidebar').toggleClass('collapsed');
    });
});
